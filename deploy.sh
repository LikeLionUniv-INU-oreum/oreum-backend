#!/bin/bash

set -e

PROJECT_DIR="/home/ubuntu/orum-backend"
CURRENT_COLOR_FILE="$PROJECT_DIR/current_color"
NGINX_CONF="/etc/nginx/sites-available/default"

cd "$PROJECT_DIR"

echo "===== Blue-Green Deploy Start ====="

echo "===== Git Update ====="
git fetch origin main
git reset --hard origin/main

echo "===== Current Color Check ====="

if [ ! -f "$CURRENT_COLOR_FILE" ]; then
  CURRENT_COLOR="legacy"
else
  CURRENT_COLOR=$(cat "$CURRENT_COLOR_FILE")
fi

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

echo "===== Docker Compose Build & Up: app-$NEXT_COLOR ====="
docker compose up -d --build app-$NEXT_COLOR

echo "===== Health Check: app-$NEXT_COLOR ====="

for i in {1..40}
do
  if curl -fs "http://localhost:$NEXT_PORT/actuator/health" | grep -q "UP"; then
    echo "Health check success: app-$NEXT_COLOR"
    break
  fi

  echo "Waiting for app-$NEXT_COLOR... $i/40"
  sleep 5

  if [ "$i" -eq 40 ]; then
    echo "Health check failed: app-$NEXT_COLOR"
    docker compose logs app-$NEXT_COLOR --tail=150
    exit 1
  fi
done

echo "===== Nginx Switch to $NEXT_PORT ====="

sudo sed -i -E "s#proxy_pass http://(localhost|127\.0\.0\.1):[0-9]+;#proxy_pass http://127.0.0.1:${NEXT_PORT};#g" "$NGINX_CONF"

sudo nginx -t
sudo nginx -s reload

echo "$NEXT_COLOR" > "$CURRENT_COLOR_FILE"

echo "===== External Health Check ====="

for i in {1..10}
do
  if curl -fs "https://api.oreumm.site/actuator/health" | grep -q "UP"; then
    echo "External health check success"
    break
  fi

  echo "Waiting for external health check... $i/10"
  sleep 3

  if [ "$i" -eq 10 ]; then
    echo "External health check failed"
    exit 1
  fi
done

echo "===== Stop Old Container ====="

if [ "$OLD_COLOR" = "blue" ]; then
  docker compose stop app-blue || true
elif [ "$OLD_COLOR" = "green" ]; then
  docker compose stop app-green || true
else
  docker stop orum-app || true
  docker rm orum-app || true
fi

echo "===== Docker Image Cleanup ====="
docker image prune -f

echo "===== Deploy Complete ====="
echo "Now running: app-$NEXT_COLOR"