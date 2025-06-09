# CORS 디버깅 체크리스트

## 1. 프론트엔드 요청 URL 확인
외부 IP에서 접근할 때 다음을 확인하세요:

```javascript
// ❌ 잘못된 예시 - localhost로 API 호출
const response = await fetch('http://localhost:8081/api/auth/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  credentials: 'include',
  body: JSON.stringify(loginData)
});

// ✅ 올바른 예시 - 실제 서버 IP로 API 호출  
const response = await fetch('http://211.187.162.65:8081/api/auth/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  credentials: 'include',
  body: JSON.stringify(loginData)
});
```

## 2. 브라우저 개발자 도구에서 확인
- Network 탭에서 OPTIONS 요청이 성공하는지 확인
- Response Headers에서 다음이 포함되어 있는지 확인:
  ```
  Access-Control-Allow-Origin: http://13.238.171.43:3000
  Access-Control-Allow-Credentials: true
  Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS, PATCH
  ```

## 3. 환경별 API 엔드포인트 설정
```javascript
// 환경에 따른 API 베이스 URL 설정
const API_BASE_URL = process.env.NODE_ENV === 'development' 
  ? 'http://localhost:8081' 
  : 'http://211.187.162.65:8081';
```

## 4. 현재 허용된 Origins
- http://localhost:3000
- http://211.187.162.65:3000  
- https://211.187.162.65:3000
- http://13.238.171.43:3000
- https://13.238.171.43:3000

## 5. Docker 재시작 후 테스트
```bash
docker-compose restart user-auth
docker-compose logs user-auth
``` 