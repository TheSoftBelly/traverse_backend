package com.taptalk.aimap.dto.travel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * 예산 정보를 전달하기 위한 DTO 클래스
 * 
 * 주요 필드:
 * - totalBudget: 총 예산 금액
 * 
 * 사용처:
 * - 여행 예산 설정
 * - 지출 내역 관리
 * - 예산 초과 알림
 */
@Getter
@Setter
public class BudgetDto {
    @NotNull(message = "총 예산은 필수 입력값입니다.")
    @Positive(message = "총 예산은 0보다 커야 합니다.")
    private Double totalBudget;
} 