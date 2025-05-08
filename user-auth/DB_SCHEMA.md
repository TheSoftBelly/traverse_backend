# DB 스키마 명세

## User

| 필드명             | 타입         | 설명            |
| ------------------ | ------------ | --------------- |
| user_id            | String (PK)  | 사용자 고유 ID  |
| user_name          | String       | 사용자 이름     |
| email              | String       | 이메일          |
| profile_picture    | String       | 프로필 사진 URL |
| post_count         | int          | 게시물 수       |
| report_count       | int          | 신고 수         |
| verify             | List<String> | 인증 정보       |
| bio                | String       | 자기소개        |
| followers          | List<String> | 팔로워 목록     |
| following          | List<String> | 팔로잉 목록     |
| created_at         | Timestamp    | 생성일          |
| last_login_at      | Timestamp    | 마지막 로그인   |
| phone_number       | String       | 전화번호        |
| location           | String       | 위치            |
| gender             | String       | 성별            |
| birthdate          | String       | 생년월일        |
| country_code       | String       | 국가코드        |
| native_language    | String       | 모국어          |
| preferred_language | String       | 선호 언어       |
| interest_keywords  | List<String> | 관심 키워드     |
| status             | String       | 계정 상태       |
| reason             | String       | 상태 변경 사유  |
| duration_days      | Integer      | 정지 기간       |
| suspended          | boolean      | 정지 여부       |

## Admin

| 필드명             | 타입      | 설명             |
| ------------------ | --------- | ---------------- |
| id                 | Long (PK) | 관리자 고유 ID   |
| name               | String    | 이름             |
| email              | String    | 이메일           |
| password           | String    | 비밀번호(암호화) |
| emailCertification | boolean   | 이메일 인증 여부 |
| passwordConfirm    | String    | 비밀번호 확인    |
| invitecode         | String    | 초대 코드        |
| role               | String    | 역할             |
| status             | String    | 상태             |
| last_login_at      | Timestamp | 마지막 로그인    |
| created_at         | Timestamp | 생성일           |
| updated_at         | Timestamp | 수정일           |

## Post

| 필드명        | 타입          | 설명            |
| ------------- | ------------- | --------------- |
| post_id       | String (PK)   | 게시물 ID       |
| user_id       | String (FK)   | 작성자 ID       |
| status        | String        | 상태            |
| created_at    | Timestamp     | 생성일          |
| report_count  | Long          | 신고 수         |
| like_count    | int           | 좋아요 수       |
| view_count    | int           | 조회수          |
| hash_tags     | List<String>  | 해시태그        |
| user_name     | String        | 작성자 이름     |
| title         | String        | 제목            |
| post_content  | String        | 내용            |
| updated_at    | Timestamp     | 수정일          |
| comment_count | int           | 댓글 수         |
| post_images   | List<String>  | 이미지 URL 목록 |
| comments      | List<Comment> | 댓글            |
| reports       | List<Report>  | 신고            |

## Report

| 필드명              | 타입          | 설명               |
| ------------------- | ------------- | ------------------ |
| report_id           | String (PK)   | 신고 ID            |
| reported_user_id    | String (FK)   | 신고 대상 ID       |
| reported_user_name  | String        | 신고 대상 이름     |
| reason              | String        | 신고 사유          |
| description         | String        | 상세 설명          |
| status              | String        | 상태               |
| category            | String        | 카테고리           |
| created_at          | LocalDateTime | 생성일             |
| updated_at          | LocalDateTime | 수정일             |
| severity            | int           | 심각도             |
| action_taken        | String        | 조치 내용          |
| comment             | String        | 관리자 코멘트      |
| notify_reporter     | boolean       | 신고자 알림 여부   |
| notify_reported     | boolean       | 피신고자 알림 여부 |
| suspension_duration | int           | 정지 기간          |
| type                | String        | 신고 유형          |
| target_id           | String        | 신고 대상 ID       |
| processed_by        | String        | 처리자             |
| processed_at        | LocalDateTime | 처리일시           |
| evidence            | List<String>  | 증거(이미지 등)    |
| reporter            | User (FK)     | 신고자             |
| reportedUser        | User (FK)     | 피신고자           |

## Hashtag

| 필드명           | 타입                 | 설명           |
| ---------------- | -------------------- | -------------- |
| id               | String (PK)          | 해시태그 ID    |
| name             | String               | 해시태그명     |
| usage_count      | int                  | 사용 횟수      |
| created_at       | Timestamp            | 생성일         |
| last_used_at     | Timestamp            | 마지막 사용일  |
| trend            | String               | 트렌드 상태    |
| trend_data       | List<TrendData>      | 트렌드 데이터  |
| related_hashtags | List<RelatedHashtag> | 연관 해시태그  |
| top_posts        | List<TopPost>        | 인기 게시글    |
| status           | String               | 상태           |
| reason           | String               | 상태 변경 사유 |

## Inquiry

| 필드명     | 타입                | 설명               |
| ---------- | ------------------- | ------------------ |
| id         | String (PK)         | 문의 ID            |
| user_id    | String (FK)         | 사용자 ID          |
| uid        | String              | UID                |
| category   | String              | 문의 카테고리      |
| message    | String              | 문의 내용          |
| status     | String              | 상태               |
| created_at | Timestamp           | 생성일             |
| answer     | Map<String, Object> | 답변(관리자, 내용) |

## InviteCode

| 필드명    | 타입          | 설명        |
| --------- | ------------- | ----------- |
| id        | Long (PK)     | 초대코드 ID |
| role      | String        | 역할        |
| code      | String        | 초대 코드   |
| expiresAt | LocalDateTime | 만료일시    |

## NotificationTemplate

| 필드명     | 타입         | 설명      |
| ---------- | ------------ | --------- |
| id         | String (PK)  | 템플릿 ID |
| name       | String       | 템플릿명  |
| type       | String       | 유형      |
| content    | String       | 내용      |
| created_at | Timestamp    | 생성일    |
| updated_at | Timestamp    | 수정일    |
| usageCount | Long         | 사용 횟수 |
| variables  | List<String> | 변수      |

## NotificationHistory

| 필드명          | 타입        | 설명         |
| --------------- | ----------- | ------------ |
| id              | String (PK) | 발송 내역 ID |
| template_id     | String      | 템플릿 ID    |
| template_name   | String      | 템플릿명     |
| type            | String      | 유형         |
| sent_at         | Timestamp   | 발송일시     |
| recipient_count | int         | 수신자 수    |
| read_count      | int         | 읽은 수      |
| sent_by         | String      | 발송자       |

</rewritten_file>
