package com.taptalk.aimap.dto.travel;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 보험 정보를 전달하기 위한 DTO 클래스
 * 
 * 주요 필드:
 * - hasTravelInsurance: 여행 보험 가입 여부
 * 
 * 사용처:
 * - 여행 보험 가입 상태 확인
 * - 보험비 예산 계산
 * - 보험 가입 추천
 */
@Getter
@Setter
public class InsuranceDto {
    @NotNull(message = "여행 보험 가입 여부는 필수 입력값입니다.")
    private Boolean hasTravelInsurance;
} 