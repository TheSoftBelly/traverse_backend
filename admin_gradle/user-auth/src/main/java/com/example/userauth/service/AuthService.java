package com.example.userauth.service;

import com.example.userauth.dto.AdminSummary;
import com.example.userauth.dto.ApiResponse;
import com.example.userauth.dto.LoginResponse;
import com.example.userauth.dto.RegisterRequest;
import com.example.userauth.model.Admin;
import com.example.userauth.repository.AdminRepository;
import com.example.userauth.security.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private final JwtTokenProvider jwtTokenProvider; // 🔹 JWT 토큰 프로바이더 추가

    @Autowired
    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;

    }

    // 비밀번호 암호화 및 사용자 등록 처리
    public boolean register(RegisterRequest registerRequest) {
        System.out.println("Received inviteCode: " + registerRequest.getInvite_code());  // 로그로 확인
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
        admin.setInvitecode(registerRequest.getInvite_code());  // 인증 코드 설정

        // invite_code에 따른 role 설정
        String role = determineRole(registerRequest.getInvite_code());
        admin.setRole(role);  // role 필드 설정

        // 3. 데이터베이스에 저장
        adminRepository.save(admin);  // Admin 정보를 DB에 저장

        return true;  // 성공적으로 저장되었으면 true 반환
    }

    private String determineRole(String inviteCode) {
        return switch (inviteCode) {
            case "chief_manager_code" -> "chief_manager";
            case "post_manager_code" -> "post_manager";
            case "chat_manager_code" -> "chat_manager";
            case "user_manager_code" -> "user_manager";
            default -> "data_manager";
        };
    }


    // 로그인 로직 (비밀번호 검증 등)
    public ApiResponse login(String email, String password) {
        Admin admin = adminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (passwordEncoder.matches(password, admin.getPassword())) {
            String token = jwtTokenProvider.generateToken(admin);
            LoginResponse loginResponse = new LoginResponse(true, "로그인 성공", admin.getEmail(), admin.getName(), token, String.valueOf(admin.getId()), admin.getRole());
            return new ApiResponse(true, "로그인 성공", loginResponse);
        } else {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
    }


    // 관리자 목록 조회 (이름과 role만 반환)
    public List<AdminSummary> getAllAdminSummaries() {
        List<Admin> admins = adminRepository.findAll();
        List<AdminSummary> adminSummaries = new ArrayList<>();

        for (Admin admin : admins) {
            AdminSummary summary =
                    new AdminSummary(admin.getName(), admin.getRole(), admin.getId(), admin.getEmail(), admin.getStatus(), admin.getLast_login_at());
            adminSummaries.add(summary);
        }

        return adminSummaries;
    }

    // 관리자 상세 정보 조회
    public Admin getAdminById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found with id " + id));
    }
}
