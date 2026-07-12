#!/bin/bash

set -e

# ===== 배포 기본 경로 및 설정 파일 경로 =====
PROJECT_DIR="/home/ubuntu/orum-backend"
CURRENT_COLOR_FILE="$PROJECT_DIR/current_color"
NGINX_CONF="/etc/nginx/sites-available/default"
DOMAIN_HEALTH_URL="https://api.oreumm.site/actuator/health"

cd "$PROJECT_DIR"

echo "===== Blue-Green Deploy Start ====="

# ===== main 브랜치 최신 코드 반영 =====
echo "===== Git Update ====="
git fetch origin main
git reset --hard origin/main

# ===== 현재 운영 중인 색상 확인 =====
echo "===== Current Color Check ====="

if [ -f "$CURRENT_COLOR_FILE" ]; then
  HAD_COLOR_FILE=true
  PREVIOUS_COLOR=$(cat "$CURRENT_COLOR_FILE")
  CURRENT_COLOR="$PREVIOUS_COLOR"
else
  HAD_COLOR_FILE=false
  PREVIOUS_COLOR=""
  CURRENT_COLOR="legacy"
fi

# ===== 현재 색상에 따라 다음 배포 대상 결정 =====
if [ "$CURRENT_COLOR" = "blue" ]; then
  NEXT_COLOR="green"
  NEXT_PORT=8082
  OLD_COLOR="blue"
elif [ "$CURRENT_COLOR" = "green" ]; then
  NEXT_COLOR="blue"
  NEXT_PORT=8081
  OLD_COLOR="green"
else
  NEXT_COLOR="blue"
  NEXT_PORT=8081
  OLD_COLOR="legacy"
fi

echo "Current color: $CURRENT_COLOR"
echo "Next color: $NEXT_COLOR"
echo "Next port: $NEXT_PORT"

# ===== 새 버전 컨테이너 빌드 및 실행 =====
echo "===== Docker Compose Build & Up: app-$NEXT_COLOR ====="
docker compose up -d --build app-$NEXT_COLOR

# ===== 새 컨테이너 내부 헬스 체크 =====
echo "===== Internal Health Check: app-$NEXT_COLOR ====="

for i in {1..40}
do
  if curl -fs "http://127.0.0.1:$NEXT_PORT/actuator/health" | grep -q "UP"; then
    echo "Internal health check success: app-$NEXT_COLOR"
    break
  fi

  echo "Waiting for app-$NEXT_COLOR... $i/40"
  sleep 5

  if [ "$i" -eq 40 ]; then
    echo "Internal health check failed: app-$NEXT_COLOR"
    docker compose logs app-$NEXT_COLOR --tail=150
    exit 1
  fi
done

# ===== Nginx 트래픽을 새 컨테이너로 전환 =====
echo "===== Nginx Switch to $NEXT_PORT ====="

NGINX_BACKUP="${NGINX_CONF}.bak"

sudo cp "$NGINX_CONF" "$NGINX_BACKUP"

sudo sed -i -E "s#proxy_pass http://(localhost|127\.0\.0\.1):[0-9]+;#proxy_pass http://127.0.0.1:${NEXT_PORT};#g" "$NGINX_CONF"

sudo nginx -t
sudo nginx -s reload

# ===== 외부 도메인 기준 헬스 체크 =====
echo "===== External Health Check ====="

EXTERNAL_OK=false

for i in {1..10}
do
  if curl -fs "$DOMAIN_HEALTH_URL" | grep -q "UP"; then
    echo "External health check success"
    EXTERNAL_OK=true
    break
  fi

  echo "Waiting for external health check... $i/10"
  sleep 3
done

# ===== 외부 헬스 체크 실패 시 Nginx와 색상 상태 롤백 =====
if [ "$EXTERNAL_OK" != "true" ]; then
  echo "External health check failed. Rolling back Nginx and color state."

  sudo cp "$NGINX_BACKUP" "$NGINX_CONF"
  sudo nginx -t
  sudo nginx -s reload

  if [ "$HAD_COLOR_FILE" = true ]; then
    echo "$PREVIOUS_COLOR" > "$CURRENT_COLOR_FILE"
  else
    rm -f "$CURRENT_COLOR_FILE"
  fi

  docker compose stop app-$NEXT_COLOR || true
  docker compose logs app-$NEXT_COLOR --tail=150 || true

  exit 1
fi

# ===== 배포 성공 후 현재 운영 색상 갱신 =====
echo "$NEXT_COLOR" > "$CURRENT_COLOR_FILE"

# ===== 이전 컨테이너 종료 =====
echo "===== Stop Old Container ====="

if [ "$OLD_COLOR" = "blue" ]; then
  docker compose stop app-blue || true
elif [ "$OLD_COLOR" = "green" ]; then
  docker compose stop app-green || true
else
  docker stop orum-app || true
  docker rm orum-app || true
fi

# ===== 사용하지 않는 Docker 이미지 정리 =====
echo "===== Docker Image Cleanup ====="
docker image prune -f

echo "===== Deploy Complete ====="
echo "Now running: app-$NEXT_COLOR"

docker ps
docker compose ps