package com.example.userauth.service;

import com.example.userauth.dto.ApiResponse;
import com.example.userauth.dto.LoginResponse;
import com.example.userauth.dto.RegisterRequest;
import com.example.userauth.model.Admin;
import com.example.userauth.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.*;

import java.util.Date;

@Service
public class AuthService {

    @Autowired
    private AdminRepository adminRepository;

    private static final String SECRET_KEY = "4JfA1PZtKp6hX9+G7/NQjZTtM2XWaHV9zPcH3UuqGqE4JfA1PZtKp6hX9+G7/NQjZTtM2XWaHV9zPcH3UuqGqE";  // 512비트 이상의 길이로 설정

    // 비밀번호 암호화 및 사용자 등록 처리
    public boolean register(RegisterRequest registerRequest) {
        System.out.println("Received inviteCode: " + registerRequest.getInviteCode());  // 로그로 확인
        // 이메일 중복 체크
        if (adminRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }
        // 1. 비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        // 2. Admin 엔티티 생성 및 값 설정
        Admin admin = new Admin();
        admin.setName(registerRequest.getName());
        admin.setEmail(registerRequest.getEmail());
        admin.setPassword(encodedPassword);// 암호화된 비밀번호 저장
        admin.setPasswordConfirm(encodedPassword);
        admin.setInvitecode(registerRequest.getInviteCode());  // 인증 코드 설정

        // invite_code에 따른 role 설정
        String role = determineRole(registerRequest.getInviteCode());
        admin.setRole(role);  // role 필드 설정

        // 3. 데이터베이스에 저장
        adminRepository.save(admin);  // Admin 정보를 DB에 저장

        return true;  // 성공적으로 저장되었으면 true 반환
    }

    private String determineRole(String inviteCode) {
        // invite_code에 따라 role을 설정 (예시)
        if ("chief_manager_code".equals(inviteCode)) {
            return "chief_manager";
        } else if ("post_manager_code".equals(inviteCode)) {
            return "post_manager";
        } else if ("chat_manager_code".equals(inviteCode)) {
            return "chat_manager";
        } else if ("user_manager_code".equals(inviteCode)) {
            return "user_manager";
        } else {
            return "data_manager";  // 기본값
        }
    }

    // 새로운 JWT 생성 함수
    private String generateNewToken(Admin admin) {
        // 사용자 이메일을 Subject로 설정
        String subject = admin.getEmail();

        // JWT Claims: 이메일, 역할, 사용자 ID 등
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("role", admin.getRole());  // role 추가
        claims.put("userId", admin.getId());  // 사용자 ID 추가 (예시)

        // JWT 생성
        return Jwts.builder()
                .setClaims(claims)  // 기존 claims 사용
                .setIssuedAt(new Date())  // 발급 일시
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))  // 24시간 유효한 토큰
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)  // HS512 서명 알고리즘 사용
                .compact();  // JWT 생성
    }

    // 로그인 로직 (비밀번호 검증 등)
    public ApiResponse login(String email, String password) {
        Admin admin = adminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // 비밀번호가 일치하는지 확인
        if (passwordEncoder.matches(password, admin.getPassword())) {
            String token = generateNewToken(admin);  // JWT 토큰 생성
            // LoginResponse 객체 생성
            LoginResponse loginResponse = new LoginResponse(true, "로그인 성공", admin.getEmail(), admin.getName(), token, String.valueOf(admin.getId()), admin.getRole());
            // ApiResponse 생성 후 반환
            return new ApiResponse(true, "로그인 성공", loginResponse);  // LoginResponse 객체를 data 필드에 포함
        } else {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
    }
}
