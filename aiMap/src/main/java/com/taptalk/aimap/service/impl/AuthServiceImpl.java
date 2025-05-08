package com.taptalk.aimap.service.impl;

import com.taptalk.aimap.dto.auth.*;
import com.taptalk.aimap.entity.User;
import com.taptalk.aimap.repository.UserRepository;
import com.taptalk.aimap.service.AuthService;
import com.taptalk.aimap.service.EmailService;
import com.taptalk.aimap.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 인증 관련 비즈니스 로직을 구현하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final FileService fileService;

    @Override
    @Transactional
    public void sendVerificationEmail(String email) {
        // TODO: 이메일 중복 체크
        String verificationCode = generateVerificationCode();
        emailService.sendVerificationEmail(email, verificationCode);
    }

    @Override
    @Transactional
    public boolean verifyEmailCode(String email, String code) {
        // TODO: 이메일 인증 코드 검증 로직 구현
        return emailService.verifyCode(email, code);
    }

    @Override
    @Transactional
    public void setPassword(SetPasswordRequest request) {
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // TODO: JWT 토큰 생성 및 반환
        return "jwt-token";
    }

    @Override
    @Transactional
    public User register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        String profileImageUrl = null;
        if (request.getProfileImage() != null) {
            profileImageUrl = uploadProfileImage(request.getProfileImage());
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .profileImageUrl(profileImageUrl)
                .emailVerified(false)
                .build();

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User registerProfile(Long userId, RegisterProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        String profileImageUrl = user.getProfileImageUrl();
        if (request.getProfileImage() != null) {
            profileImageUrl = uploadProfileImage(request.getProfileImage());
        }

        user.setName(request.getName());
        user.setGender(request.getGender());
        user.setBirthDate(request.getBirthDate());
        user.setProfileImageUrl(profileImageUrl);

        return userRepository.save(user);
    }

    @Override
    public String uploadProfileImage(MultipartFile file) {
        // TODO: 파일 업로드 로직 구현
        return "profile-image-url";
    }

    private String generateVerificationCode() {
        // TODO: 랜덤 인증 코드 생성 로직 구현
        return "123456";
    }
} 