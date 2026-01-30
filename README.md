# 🥔 POTATO PROJECT

<br>

## 🛠 Backend Role & Responsibilities


### 👩‍💻 : Auth & Community

| Category | Details |
| :--- | :--- |
| **Auth** | - Spring Security + **JWT** 기반 인증 시스템 구축 <br> - 회원가입/로그인 및 프로필 관리 API |
| **Community** | - 게시글 CRUD(작성/조회/수정/삭제) 로직 설계 <br> - 댓글 시스템 및 좋아요 상호작용 기능 구현 |
| **Setting** | - 프로젝트 초기 환경 설정 및 아키텍처 구조화 |

<br>

### 👨‍💻 : Shop & Economy

| Category | Details |
| :--- | :--- |
| **Shop API** | - 33종 아이템 데이터베이스(MariaDB) 구축 <br> - **카테고리별 필터링** 및 아이템 상세 조회 API 개발 |
| **Economy** | - 유저 재화(Spoon) 소모 로직 및 **구매 시스템** 구현 <br> - 유저별 인벤토리 관리 및 아이템 장착 로직 설계 |
| **Infra** | - **CORS 설정**: 프론트엔드 통신 허용 <br> - **이미지 서버**: 정적 리소스 매핑 및 서빙 환경 구축 |

---

### 🚀 Tech Stack
![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=flat-square&logo=springboot)
![MariaDB](https://img.shields.io/badge/MariaDB-latest-blue?style=flat-square&logo=mariadb)
![Swagger](https://img.shields.io/badge/Swagger-API%20Docs-85EA2D?style=flat-square&logo=swagger)

---

### 🔗 주요 API 명세 (Shop)
* `GET /api/items` : 전체 아이템 목록
* `GET /api/items/category/{category}` : 카테고리별 아이템 (HEADWEAR, OUTFIT 등)
* `GET /images/{fileName}` : 아이템 이미지 리소스 서빙
