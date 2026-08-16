# 🥔 POTATO

POTATO는 자취생이 직접 요리한 식사를 인증하고, 보상으로 받은 경험치와 재화 `스푼`으로 캐릭터를 성장시키는 식생활 습관 형성 서비스입니다.

## 🧭 시스템 작동 방식

1. 사용자가 직접 요리한 식사를 인증합니다.
2. 인증 결과에 따라 경험치와 서비스 재화인 스푼을 지급합니다.
3. 사용자는 스푼으로 상점 아이템을 구매합니다.
4. 구매한 아이템을 인벤토리에서 장착해 캐릭터를 성장시킵니다.

```text
요리 인증 → 경험치·스푼 지급 → 아이템 구매 → 인벤토리 저장 → 캐릭터 장착
```

## 📦 저장소

- [Backend](https://github.com/POTATO-119/Backend): Spring Boot 기반 API 서버
- [Frontend](https://github.com/POTATO-119/Frontend): React와 Vite 기반 웹 클라이언트

## 📁 폴더 구조

```text
POTATO-119/
├── Backend/
│   ├── src/main/java/com/example/potato/
│   │   ├── controller/      # HTTP 요청 및 응답 처리
│   │   ├── service/         # 구매, 인벤토리 등 비즈니스 규칙
│   │   ├── repository/      # 데이터베이스 접근
│   │   ├── entity/          # 사용자, 아이템, 인벤토리 모델
│   │   ├── dto/             # API 요청·응답 데이터
│   │   └── config/          # CORS 및 애플리케이션 설정
│   ├── src/main/resources/  # DB 및 Spring Boot 설정
│   └── src/test/            # 백엔드 테스트
└── Frontend/
    ├── src/pages/           # 로그인, 회원가입, 홈 화면
    ├── src/features/        # 인증 등 도메인별 기능
    ├── src/components/      # 공통 및 화면 구성 컴포넌트
    ├── src/lib/             # Axios API 클라이언트
    ├── src/router/          # 화면 경로 설정
    ├── src/store/           # 클라이언트 상태 관리
    └── src/assets/          # 이미지와 정적 리소스
```

## 🛠 기술 스택

- **Backend:** Java 21, Spring Boot 4, Spring Data JPA, Gradle
- **Database:** MySQL
- **Frontend:** React 18, TypeScript, Vite

## 🚀 로컬 실행 가이드

### 1. 저장소 클론

```bash
git clone https://github.com/POTATO-119/Backend.git potato-backend
git clone https://github.com/POTATO-119/Frontend.git potato-frontend
```

### 2. 실행 환경 준비

- Java 21
- MySQL 8.x
- Node.js 20 이상
- npm

### 3. 데이터베이스 및 환경 변수 설정

MySQL에서 데이터베이스를 생성합니다.

```sql
CREATE DATABASE potato CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

백엔드 실행 터미널에 데이터베이스 접속 정보를 설정합니다. Spring Boot 환경 변수가 저장소의 기본 설정보다 우선 적용됩니다.

```bash
export SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3306/potato?serverTimezone=Asia/Seoul&characterEncoding=UTF-8"
export SPRING_DATASOURCE_USERNAME="root"
export SPRING_DATASOURCE_PASSWORD="your-mysql-password"
```

프론트엔드에서는 예제 파일을 복사한 뒤 로컬 API 주소를 설정합니다.

```bash
cd potato-frontend
cp .env.example .env.local
```

`.env.local`의 값을 다음과 같이 변경합니다.

```dotenv
VITE_API_BASE_URL=http://localhost:8080
VITE_API_TIMEOUT=5000
VITE_APP_NAME=POTATO
VITE_APP_ENV=development
```

### 4. 백엔드 실행

```bash
cd potato-backend
bash ./gradlew bootRun
```

서버가 실행되면 다음 주소에서 API 문서를 확인할 수 있습니다.

- Swagger UI: <http://localhost:8080/swagger-ui/index.html>

### 5. 프론트엔드 의존성 설치 및 실행

새 터미널에서 다음 명령을 실행합니다.

```bash
cd potato-frontend
npm install
npm run dev
```

- Frontend: <http://localhost:5173>

### 6. 실행 확인

- 백엔드 Swagger UI가 열리는지 확인합니다.
- 프론트엔드 로그인 화면이 표시되는지 확인합니다.
- 회원가입 또는 로그인 요청이 `http://localhost:8080`으로 전달되는지 브라우저 개발자 도구에서 확인합니다.
- 상점 조회 → 아이템 구매 → 인벤토리 조회 → 아이템 장착 순서로 데이터가 연결되는지 확인합니다.

## 🔐 구매 데이터 정합성

- 구매 전에 사용자·아이템 존재 여부, 중복 보유 여부, 보유 재화를 검증합니다.
- 재화 차감과 인벤토리 생성을 하나의 트랜잭션으로 처리합니다.
- 구매 실패 시 재화만 차감되거나 아이템만 지급되는 부분 반영을 방지합니다.
- 사용자 소유권을 확인하고 카테고리별 장착 아이템을 하나로 유지합니다.

## 🔗 주요 API

- `POST /api/users/join`: 회원가입
- `POST /api/users/login`: 로그인
- `GET /api/items`: 전체·카테고리별 아이템 조회
- `POST /api/items/purchase`: 아이템 구매 및 재화 차감
- `GET /api/items/inventory/{userId}`: 사용자 인벤토리 조회
- `POST /api/items/inventory/equip`: 아이템 장착 및 기존 아이템 자동 해제
