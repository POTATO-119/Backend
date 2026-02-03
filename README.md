# 🥔 POTATO PROJECT


### 🛠 Backend Role & Responsibilities


## 🐰 : Auth & Community

| Category | Details |
| :--- | :--- |
| **Auth** | - 회원가입/로그인 및 유저 프로필 관리 API <br> - 성장 로직: 유저 경험치(XP) 및 레벨(Level) 데이터 처리 <br> |
| **Community** | - 게시글 CRUD(작성/조회/수정/삭제) 기능 구현 <br> - 사용자별 활동 이력 관리 및 작성 글 모아보기 |
| **Setting** | - 프로젝트 초기 환경 설정 및 공통 응답 구조화 |



## 🔗 주요 API 명세서 (User)
* `POST /api/users/join` : 회원가입 (초기 스푼 및 레벨 설정)
* `POST /api/users/login` : 로그인 및 사용자 인증
* `GET /api/users/info/{loginId}` : 내 정보 조회 (XP, Spoon 데이터 포함)

<br>
---

### 👽 : Shop & Economy

| Category | Details |
| :--- | :--- |
| **Shop API** | - 상점 아이템 데이터베이스(MariaDB) 구축 및 ERD 설계 <br> - 자산 관리: 유저 재화(Spoon) 관리 및 상점 연동 인터페이스 |
| **Economy** | - 트랜잭션(`@Transactional`) 기반 유저 재화(Spoon) 차감 및 구매 시스템 구현 <br> - 유저별 인벤토리 관리 및 중복 구매 방지 예외 처리 적용 |
| **Infra** | - CORS 설정: 프론트엔드-백엔드 리소스 공유 및 통신 허용 <br> - Swagger UI: API 명세 표준화 및 테스트 환경 구축 |

---

## 🔗 주요 API 명세서 (Shop)
* `GET /api/items` : 전체 아이템 목록 및 카테고리별 필터링 조회
* `POST /api/items/purchase` : 아이템 구매 및 재화(Spoon) 차감
* `GET /api/items/inventory/{userId}` : 유저별 인벤토리(보유 아이템) 조회
* `POST /api/items/inventory/equip` : 아이템 장착 및 자동 교체 로직 실행
  
---

### 🚀 Tech Stack
![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-brightgreen?style=flat-square&logo=springboot)
![MariaDB](https://img.shields.io/badge/MariaDB-latest-blue?style=flat-square&logo=mariadb)
![Swagger](https://img.shields.io/badge/Swagger-API%20Docs-85EA2D?style=flat-square&logo=swagger)

