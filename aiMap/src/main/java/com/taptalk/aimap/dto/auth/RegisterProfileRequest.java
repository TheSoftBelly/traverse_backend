package com.taptalk.aimap.dto.auth;

import com.taptalk.aimap.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterProfileRequest {
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    @NotNull(message = "성별은 필수 입력값입니다.")
    private User.Gender gender;

    @NotNull(message = "생년월일은 필수 입력값입니다.")
    @Past(message = "생년월일은 현재 날짜보다 이전이어야 합니다.")
    private LocalDate birthDate;

    private MultipartFile profileImage;
} 