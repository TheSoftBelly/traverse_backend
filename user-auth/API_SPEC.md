# API 명세서 (상세)

## 인증 및 사용자 관리

### 회원가입

- **POST** `/api/auth/register`
- **설명**: 신규 사용자 회원가입
- **RequestBody** 예시:
  ```json
  {
    "email": "user@example.com",
    "password": "password123",
    "userName": "홍길동"
  }
  ```
- **성공 응답** (`200 OK`)
  ```json
  {
    "success": true,
    "message": "회원가입 성공"
  }
  ```
- **에러 응답** (`400 Bad Request`)
  ```json
  {
    "success": false,
    "message": "이미 존재하는 이메일입니다."
  }
  ```
- **비고**: 이메일 중복, 비밀번호 정책 등 검증 실패 시 상세 메시지 반환

### 로그인

- **POST** `/api/auth/login`
- **설명**: 이메일/비밀번호로 로그인, JWT 토큰 발급
- **RequestBody** 예시:
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```
- **성공 응답** (`200 OK`)
  ```json
  {
    "success": true,
    "data": {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "user": {
        "user_id": "abc123",
        "user_name": "홍길동",
        "email": "user@example.com"
      }
    }
  }
  ```
- **에러 응답** (`401 Unauthorized`)
  ```json
  {
    "success": false,
    "message": "이메일 또는 비밀번호가 올바르지 않습니다."
  }
  ```

### 관리자 목록 조회

- **GET** `/api/auth/admins`
- **설명**: 관리자 요약 목록 반환
- **성공 응답** (`200 OK`)
  ```json
  [
    { "id": 1, "name": "관리자1", "role": "SUPER_ADMIN" },
    { "id": 2, "name": "관리자2", "role": "ADMIN" }
  ]
  ```

### 관리자 상세 조회

- **GET** `/api/auth/admins/{adminId}`
- **설명**: 관리자 상세 정보 반환
- **성공 응답** (`200 OK`)
  ```json
  {
    "success": true,
    "data": {
      "id": 1,
      "name": "관리자1",
      "email": "admin@example.com",
      "role": "SUPER_ADMIN",
      "status": "active",
      "last_login_at": "2024-06-01T12:34:56Z",
      "created_at": "2024-05-01T10:00:00Z",
      "updated_at": "2024-06-01T12:34:56Z",
      "activity_logs": [
        {
          "action": "LOGIN",
          "timestamp": "2024-06-01T12:34:56Z",
          "meta": { "ip": "192.168.0.1" }
        }
      ]
    }
  }
  ```
- **에러 응답** (`404 Not Found`)
  ```json
  { "success": false, "message": "관리자를 찾을 수 없습니다." }
  ```

### 관리자 역할/상태 변경

- **PATCH** `/api/admins/{admin_id}/role`
- **RequestBody** 예시:
  ```json
  { "role": "ADMIN" }
  ```
- **성공 응답** (`200 OK`)
  ```json
  { "success": true, "message": "관리자 역할이 변경되었습니다" }
  ```
- **에러 응답** (`403 Forbidden`)
  ```json
  { "success": false, "message": "권한이 없습니다." }
  ```

### 사용자 목록 조회

- **GET** `/api/users`
- **설명**: 전체 사용자 목록 조회 (관리자 권한 필요)
- **Query 파라미터**:
  - `page` (int, 기본값 1): 페이지 번호
  - `limit` (int, 기본값 20): 페이지당 개수
  - `search` (string, optional): 이름/이메일 검색
  - `status` (string, optional): 계정 상태 필터
  - `sortBy` (string, 기본값 created_at): 정렬 필드
  - `sortOrder` (string, 기본값 desc): 정렬 순서
- **성공 응답** (`200 OK`)
  ```json
  {
    "success": true,
    "users": [
      {
        "user_id": "abc123",
        "user_name": "홍길동",
        "email": "user@example.com",
        "verify": ["email"],
        "created_at": "2024-06-01T12:34:56Z",
        "last_login_at": "2024-06-01T13:00:00Z",
        "report_count": 0,
        "country_code": "KR"
      }
    ],
    "total_count": 100,
    "current_page": 1,
    "total_pages": 5
  }
  ```
- **에러 응답** (`403 Forbidden`)
  ```json
  {
    "success": false,
    "message": "권한이 없습니다."
  }
  ```

### 사용자 상세 조회

- **GET** `/api/users/{userId}`
- **설명**: 사용자 상세 정보
- **Path 파라미터**: `userId` (string)
- **성공 응답** (`200 OK`)
  ```json
  {
    "user_id": "abc123",
    "user_name": "홍길동",
    "email": "user@example.com",
    "profile_picture": "https://...",
    "post_count": 10,
    "report_count": 0,
    "verify": ["email"],
    "bio": "소개글",
    "followers": ["user2"],
    "following": ["user3"],
    "created_at": "2024-06-01T12:34:56Z",
    "last_login_at": "2024-06-01T13:00:00Z",
    "phone_number": "010-1234-5678",
    "location": "Seoul",
    "gender": "male",
    "birthdate": "1990-01-01",
    "country_code": "KR",
    "native_language": "ko",
    "preferred_language": "en",
    "interest_keywords": ["flutter", "dart"],
    "status": "active",
    "reason": null,
    "duration_days": null,
    "suspended": false
  }
  ```
- **에러 응답** (`404 Not Found`)
  ```json
  {
    "success": false,
    "message": "User not found"
  }
  ```

### 사용자 상태 변경

- **PATCH** `/api/users/{user_id}/status`
- **설명**: 사용자 계정 상태 변경(정지, 해제 등)
- **RequestBody** 예시:
  ```json
  {
    "status": "suspended",
    "reason": "욕설",
    "durationDays": 7
  }
  ```
- **성공 응답** (`200 OK`)
  ```json
  {
    "success": true,
    "message": "상태가 변경되었습니다."
  }
  ```
- **에러 응답** (`400 Bad Request`)
  ```json
  {
    "success": false,
    "message": "유효하지 않은 상태입니다."
  }
  ```

### 사용자 통계

- **GET** `/api/users/statistics`
- **설명**: 사용자 통계 대시보드 데이터
- **Query**: `start_date`, `end_date`, `interval`
- **성공 응답** (`200 OK`)
  ```json
  {
    "total_users": 1000,
    "active_users": 900,
    "new_users": 50
  }
  ```

### 사용자 탈퇴 사유 통계

- **GET** `/api/users/withdrawal-reasons`
- **설명**: 기간별 탈퇴 사유 통계
- **Query**: `start_date`, `end_date`
- **성공 응답** (`200 OK`)
  ```json
  {
    "reasons": [
      { "reason": "서비스 불만", "count": 10 },
      { "reason": "기타", "count": 5 }
    ]
  }
  ```

## 관리자/권한

### 관리자 역할 변경

- **PATCH** `/api/admins/{admin_id}/role`
- **설명**: 관리자 역할 변경
- **RequestBody**: `{ role }`
- **Response**: `{ success, message }`

## 초대 코드

### 초대 코드 생성

- **POST** `/api/auth/invite-code/generate`
- **설명**: 관리자 초대 코드 생성
- **RequestBody**: `{ email, role }`
- **Response**: `{ success, data: { code, expires_at }, message }`

### 초대 코드 검증

- **POST** `/api/auth/invite-code/validate`
- **설명**: 초대 코드 유효성 검증
- **RequestBody**: `{ code }`
- **Response**: `{ ... }`

## 게시물

### 게시물 목록 조회

- **GET** `/api/posts`
- **설명**: 게시물 목록, 페이징/검색/정렬 지원
- **Query**: `page`, `limit`, `status`, `search`, `user_id`, `sort_by`, `sort_order`, `has_reports`
- **성공 응답** (`200 OK`)
  ```json
  {
    "success": true,
    "posts": [
      {
        "post_id": "post123",
        "title": "첫 게시물",
        "user_id": "abc123",
        "user_name": "홍길동",
        "status": "active",
        "created_at": "2024-06-01T12:34:56Z",
        "updated_at": "2024-06-01T13:00:00Z",
        "report_count": 0,
        "like_count": 10,
        "view_count": 100,
        "comment_count": 2,
        "hash_tags": ["flutter", "dart"],
        "post_images": ["https://..."],
        "comments": [
          {
            "comment_id": "c1",
            "user_id": "u2",
            "user_name": "댓글러",
            "content": "댓글 내용",
            "created_at": "2024-06-01T13:10:00Z",
            "report_count": 0
          }
        ],
        "reports": [
          {
            "report_id": "r1",
            "reason": "욕설",
            "status": "pending"
          }
        ]
      }
    ],
    "total_count": 100,
    "current_page": 1,
    "total_pages": 5
  }
  ```

### 게시물 상세 조회

- **GET** `/api/posts/{post_id}`
- **설명**: 게시물 상세 정보
- **Path 파라미터**: `post_id` (string)
- **성공 응답** (`200 OK`)
  ```json
  {
    "post_id": "post123",
    "title": "첫 게시물",
    "post_content": "내용입니다.",
    "user_id": "abc123",
    "user_name": "홍길동",
    "status": "active",
    "created_at": "2024-06-01T12:34:56Z",
    "updated_at": "2024-06-01T13:00:00Z",
    "report_count": 0,
    "like_count": 10,
    "view_count": 100,
    "comment_count": 2,
    "hash_tags": ["flutter", "dart"],
    "post_images": ["https://..."],
    "comments": [ ... ],
    "reports": [ ... ]
  }
  ```
- **에러 응답** (`404 Not Found`)
  ```json
  {
    "success": false,
    "message": "게시물을 찾을 수 없습니다."
  }
  ```

### 게시물 상태 변경

- **PATCH** `/api/posts/{post_id}/status`
- **설명**: 게시물 상태 변경
- **RequestBody** 예시:
  ```json
  {
    "status": "hidden"
  }
  ```
- **성공 응답** (`200 OK`)
  ```json
  {
    "success": true,
    "message": "게시물 상태가 변경되었습니다."
  }
  ```

### 게시물 신고 검토 대기 목록

- **GET** `/api/posts/pending-reviews`
- **설명**: 신고 검토 대기 게시물 목록
- **Query**: `page`, `limit`
- **성공 응답** (`200 OK`)
  ```json
  {
    "success": true,
    "pending_reviews": [ ... ],
    "total_count": 10,
    "current_page": 1,
    "total_pages": 1
  }
  ```

### 게시물 통계

- **GET** `/api/posts/statistics`
- **설명**: 게시물 통계 대시보드 데이터
- **Query**: `start_date, end_date, interval`
- **성공 응답** (`200 OK`)
  ```json
  {
    "success": true,
    "statistics": { "total_posts": 100, "active_posts": 90 }
  }
  ```

## 신고

### 사용자 신고 목록

- **GET** `/api/reports/users`
- **설명**: 사용자 신고 목록
- **Query**: `page, limit, search, status, sortBy, sortOrder`
- **성공 응답** (`200 OK`)
  ```json
  {
    "success": true,
    "reports": [
      {
        "report_id": "r1",
        "reported_user_id": "u2",
        "reported_user_name": "신고대상",
        "reporter_user_id": "u1",
        "reason": "욕설",
        "description": "욕설 신고",
        "status": "pending",
        "created_at": "2024-06-01T13:00:00Z",
        "updated_at": "2024-06-01T13:10:00Z",
        "severity": 1,
        "type": "user",
        "target_id": "u2",
        "evidence": ["https://..."],
        "processed_by": null,
        "processed_at": null,
        "comment": null,
        "action_taken": null
      }
    ],
    "total_count": 10,
    "current_page": 1,
    "total_pages": 1
  }
  ```

### 게시물 신고 목록

- **GET** `/api/reports/posts`
- **설명**: 게시물 신고 목록
- **Query**: `page, limit, search, status, sortBy, sortOrder`
- **Response**: `{ ... }`

### 채팅 신고 목록

- **GET** `/api/reports/chats`
- **설명**: 채팅 신고 목록
- **Query**: `page, limit, search, status, sortBy, sortOrder`
- **Response**: `{ ... }`

### 신고 상세 조회

- **GET** `/api/reports/{reportId}`
- **설명**: 신고 상세 정보
- **Response**: `{ ... }`

### 신고 상태 변경

- **PATCH** `/api/reports/{report_id}/status`
- **설명**: 신고 상태 변경
- **RequestBody**: `{ status, reason }`
- **Response**: `{ success, message }`

### 신고 처리

- **PATCH** `/api/reports/{report_id}/process`
- **설명**: 신고 처리(상태 변경, 조치, 알림 등)
- **RequestBody** 예시:
  ```json
  {
    "status": "resolved",
    "actionTaken": "사용자 7일 정지",
    "comment": "욕설로 인한 정지",
    "notifyReporter": true,
    "notifyReported": true,
    "suspensionDuration": 7
  }
  ```
- **성공 응답** (`200 OK`)
  ```json
  {
    "success": true,
    "message": "신고가 처리되었습니다"
  }
  ```
- **에러 응답** (`400 Bad Request`)
  ```json
  {
    "success": false,
    "message": "이미 처리된 신고입니다."
  }
  ```

## 알림

### 알림 템플릿 목록

- **GET** `/api/notifications/templates`
- **설명**: 알림 템플릿 목록
- **Query**: `page, limit, type`
- **Response**: `{ ... }`

### 알림 템플릿 생성

- **POST** `/api/notifications/templates`
- **설명**: 알림 템플릿 생성
- **RequestBody** 예시:
  ```json
  {
    "name": "경고 알림",
    "type": "warning",
    "content": "{{userName}}님, 경고 알림입니다.",
    "variables": ["userName"]
  }
  ```
- **성공 응답** (`200 OK`)
  ```json
  {
    "success": true,
    "message": "알림 템플릿이 저장되었습니다",
    "data": { "id": "template123" }
  }
  ```

### 알림 템플릿 수정

- **PUT** `/api/notifications/templates`
- **설명**: 알림 템플릿 수정
- **RequestBody**: `{ ... }`
- **Response**: `{ success, message, data: { id } }`

### 알림 발송

- **POST** `/api/notifications/send`
- **설명**: 알림 발송
- **RequestBody**: `{ ... }`
- **Response**: `{ ... }`

### 알림 발송 내역

- **GET** `/api/notifications/history`
- **설명**: 알림 발송 내역
- **Query**: `page, limit, type, start_date, end_date`
- **Response**: `{ ... }`

## 해시태그

### 해시태그 목록

- **GET** `/api/hashtags`
- **설명**: 해시태그 목록
- **Query**: `page`, `limit`, `search`, `sortBy`, `sortOrder`, `type`
- **성공 응답** (`200 OK`)
  ```json
  {
    "success": true,
    "hashtags": [
      {
        "id": "tag1",
        "name": "flutter",
        "usage_count": 100,
        "created_at": "2024-06-01T12:34:56Z",
        "last_used_at": "2024-06-01T13:00:00Z",
        "trend": "rising"
      }
    ],
    "total_count": 100,
    "current_page": 1,
    "total_pages": 5
  }
  ```

### 해시태그 상세

- **GET** `/api/hashtags/{hashtag_id}`
- **설명**: 해시태그 상세 정보
- **Response**: `{ ... }`

### 해시태그 상태 변경

- **PATCH** `/api/hashtags/{hashtag_id}/status`
- **설명**: 해시태그 상태 변경
- **RequestBody**: `{ status, reason }`
- **Response**: `{ success, message }`

### 해시태그 통계

- **GET** `/api/hashtags/statistics`
- **설명**: 해시태그 통계 대시보드 데이터
- **Query**: `start_date, end_date`
- **Response**: `{ ... }`

### 해시태그 워드클라우드

- **GET** `/api/hashtags/wordcloud`
- **설명**: 워드클라우드용 해시태그 데이터
- **Query**: `limit, period`
- **Response**: `{ ... }`

## 문의

### 문의 목록

- **GET** `/api/admins/inquiries`
- **설명**: 문의 내역 목록
- **Query**: `page`, `limit`, `status`
- **성공 응답** (`200 OK`)
  ```json
  {
    "success": true,
    "inquiries": [
      {
        "id": "inq1",
        "user_id": "abc123",
        "category": "기타",
        "message": "문의 내용",
        "status": "open",
        "created_at": "2024-06-01T12:34:56Z"
      }
    ],
    "total_count": 10,
    "current_page": 1,
    "total_pages": 1
  }
  ```

### 문의 상세

- **GET** `/api/admins/inquiries/{inquiryId}`
- **설명**: 문의 상세 정보
- **Response**: `{ ... }`

### 문의 상태 변경

- **PATCH** `/api/admins/inquiries/{inquiryId}/status`
- **설명**: 문의 상태 변경
- **RequestBody**: `{ status }`
- **Response**: `{ ... }`

### 문의 답장

- **POST** `/api/admins/inquiries/{inquiryId}/reply`
- **설명**: 문의 답장
- **RequestBody**: `{ message }`
- **Response**: `{ ... }`

## Firestore (관리자용)

### 컬렉션 문서 조회

- **GET** `/api/firestore/collection/{collectionName}/document/{documentId}`
- **설명**: Firestore 컬렉션의 특정 문서 조회
- **성공 응답** (`200 OK`)
  ```json
  {
    "field1": "value1",
    "field2": "value2"
  }
  ```
- **에러 응답** (`404 Not Found`)
  ```json
  {
    "success": false,
    "message": "문서를 찾을 수 없습니다."
  }
  ```

### 문서 필드 조회

- **GET** `/api/firestore/collection/{collectionName}/document/{documentId}/field/{fieldName}`
- **설명**: Firestore 문서의 특정 필드 값 조회
- **Response**: `{ ... }`
