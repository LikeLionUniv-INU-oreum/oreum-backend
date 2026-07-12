# # Oreum Backend

Oreum 서비스의 백엔드 서버 레포지토리입니다.

</br>

## 🛠️ 기술 스택

- Java 21
- Spring Boot 4.x
- Spring Security
- Spring Data JPA
- MySQL 8.0
- JWT
- Gradle
- Docker
- GitHub Actions
- AWS EC2

</br>

## ✨ 주요 기능

- 이메일 인증 기반 회원가입
- JWT 기반 로그인 인증
- 사용자 온보딩 및 프로필 관리
- 학기별 Todo 관리
- 베이스캠프 대시보드 조회
- 코스 리뷰 작성 및 탐색
- 리뷰 좋아요
- STAR 카드 기록
- 마이페이지/설정창 사용자 정보 조회

</br>

## 🗂️ 프로젝트 구조

```text
src/main/java/com/likelion/orum
├── domain
│   ├── auth
│   ├── user
│   ├── term
│   ├── common
│   ├── home
│   ├── todo
│   ├── review
│   ├── starcard
│   ├── category
│   ├── major
│   └── job
├── global
│   ├── config
│   ├── exception
│   ├── init
│   ├── response
│   └── security
└── OrumApplication.java
```

</br>

## 📦 패키지 설명

| 패키지 | 설명 |
|---|---|
| `domain.auth` | 회원가입, 로그인, 이메일 인증, JWT 발급 등 인증 관련 기능을 담당합니다. |
| `domain.user` | 사용자 계정, 사용자 프로필, 온보딩, 설정창 사용자 정보 관리를 담당합니다. |
| `domain.term` | 사용자의 학기/분기 정보와 베이스캠프 대시보드 조회를 담당합니다. |
| `domain.todo` | 사용자의 목표 Todo 생성, 조회, 수정, 완료 처리 및 삭제를 담당합니다. |
| `domain.review` | 코스 리뷰 작성, 조회, 탐색, 추천 시기, 리뷰 좋아요 기능을 담당합니다. |
| `domain.starcard` | 완료한 활동에 대한 STAR 형식 기록 관리를 담당합니다. |
| `domain.category` | Todo 및 리뷰에서 사용하는 활동 카테고리와 점수 정보를 관리합니다. |
| `domain.major` | 사용자 온보딩 및 프로필에서 사용하는 전공 정보를 관리합니다. |
| `domain.job` | 사용자 온보딩 및 프로필에서 사용하는 희망 직무 정보를 관리합니다. |
| `domain.common` | 여러 도메인에서 공통으로 사용하는 인증 사용자 정보, 공통 타입 등을 관리합니다. |
| `domain.home` | 홈 화면 또는 서비스 진입 화면에서 필요한 요약 정보를 제공합니다. |
| `global.config` | Spring Security, CORS, Swagger 등 전역 설정을 관리합니다. |
| `global.exception` | 공통 예외 타입, 에러 코드, 예외 처리 로직을 관리합니다. |
| `global.init` | 애플리케이션 실행 시 필요한 초기 데이터 설정을 관리합니다. |
| `global.response` | 공통 API 응답 형식을 관리합니다. |
| `global.security` | JWT 인증 필터, 토큰 처리, 인증 관련 보안 로직을 관리합니다. |

<br/>

## 🚀 실행 방법
1. Repository clone
   - `git clone https://github.com/LikelionUniv-INU-oreum/orum-backend.git`
   - `cd orum-backend`


2. 환경변수 설정
   - 프로젝트 루트에 .env 파일을 생성합니다.
        ```text
        DB_HOST=localhost
        DB_PORT=3306
        DB_NAME=orum
        DB_USERNAME=root
        DB_PASSWORD=password
    
        JWT_SECRET_KEY=your-secret-key
    
        GOOGLE_SMTP_EMAIL=your-email@gmail.com
        GOOGLE_SMTP_APP_PASSWORD=your-app-password
        ```


3. MySQL 실행
   - `docker compose up -d mysql`


4. 애플리케이션 실행
   - `./gradlew bootRun`

     또는 테스트 포함 빌드:

   - `./gradlew clean build`

<br/>

## 📡 API 응답 형식

성공 응답

```json
{
  "isSuccess": true,
  "code": "COMMON_200",
  "message": "요청에 성공했습니다.",
  "result": {}
}
```

실패 응답

```json
{
  "isSuccess": false,
  "code": "COMMON_400",
  "message": "잘못된 요청입니다.",
  "result": null
}
```

<br/>

## 🗄️ 데이터베이스

### ERD

<img width="2896" height="1206" alt="Oreum ERD" src="https://github.com/user-attachments/assets/3bd15aea-99e9-4ba2-97b7-b29b79078524" />

<br/>

### 주요 테이블

| 테이블 | 설명 |
|---|---|
| `email_verification` | 회원가입 및 비밀번호 재설정 과정에서 사용하는 이메일 인증 정보를 저장합니다. 인증 이메일, 인증 코드 해시, 인증 상태, 만료 시간, 인증 목적을 관리합니다. |
| `users` | 사용자 계정 정보를 저장합니다. 학교 이메일, 비밀번호 해시, 닉네임, 온보딩 완료 여부, 사용자 상태를 관리합니다. |
| `user_profiles` | 사용자 프로필 정보를 저장합니다. 사용자 계정과 전공, 희망 직무, 학적 상태를 연결합니다. |
| `jobs` | 사용자가 선택할 수 있는 희망 직무 정보를 저장합니다. |
| `majors` | 사용자가 선택할 수 있는 전공 및 단과대학 정보를 저장합니다. |
| `terms` | 사용자 프로필에 연결된 연도별 상반기/하반기 정보를 저장합니다. 베이스캠프 대시보드와 Todo를 분기 단위로 관리하기 위한 기준 테이블입니다. |
| `todos` | 사용자가 설정한 목표 활동을 저장합니다. 활동명, 주차별 계획, 진행 상태, 완료 시간, 카테고리, 분기 정보를 관리합니다. |
| `categories` | Todo와 리뷰에 연결되는 활동 카테고리 정보를 저장합니다. 각 카테고리는 점수를 가지며, 완료한 Todo의 점수 합산에 사용됩니다. |
| `course_reviews` | 완료한 Todo에 대한 코스 리뷰를 저장합니다. 별점, 등반 학년, 등반 학기, 소요 기간, 팁, 공개 상태를 관리합니다. |
| `review_recommended_grades` | 특정 리뷰가 어떤 학년에게 추천되는지 저장합니다. 하나의 리뷰에 여러 추천 학년을 연결할 수 있습니다. |
| `review_likes` | 사용자가 리뷰에 누른 좋아요 정보를 저장합니다. 사용자와 리뷰를 연결하여 중복 좋아요를 방지합니다. |
| `star_cards` | 리뷰에 연결된 STAR 형식 기록을 저장합니다. 상황, 과제, 행동, 결과를 구조화해 관리합니다. |

<br/>

## 🌿 브랜치 전략

| 브랜치 | 설명|
|----|----|
| main    | 배포 브랜치 |
| dev     | 개발 통합 브랜치 |
| feat/*  | 기능 개발 브랜치 |
| fix/*   | 버그 수정 브랜치 |
| hotfix/* | 긴급 수정 브랜치 |

<br/>

## 📝 커밋 컨벤션

```text
<type>(<domain>): <subject>
```

예시:
```
feat(auth): 로그인 API 구현
fix(todo): 진행중인 일 부분 수정 처리
refactor(review): 리뷰 조회 로직 정리
chore: dev 변경사항 main 병합
```

<br/>

## 🔀 PR 규칙

- 기능 단위로 PR을 생성합니다.

- PR은 dev 브랜치를 대상으로 생성합니다.

- 배포 반영 시 dev → main PR을 생성합니다.

- main 반영 후 main → dev 동기화 PR을 생성합니다.

<br/>

## 🚢 배포

- GitHub Actions를 통해 main 브랜치 변경사항을 EC2 서버에 자동 배포합니다.

- EC2 서버에서는 Docker Compose 기반으로 애플리케이션과 MySQL을 실행합니다.

<br/>
