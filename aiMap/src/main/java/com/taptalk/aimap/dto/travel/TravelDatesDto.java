package com.taptalk.aimap.dto.travel;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 여행 기간 정보를 전달하기 위한 DTO 클래스
 * 
 * 주요 필드:
 * - startDate: 여행 시작일
 * - endDate: 여행 종료일
 * 
 * 사용처:
 * - 여행 계획 생성 시 여행 기간 설정
 * - 일정 관리 시 날짜 범위 검증
 */
@Getter
@Setter
public class TravelDatesDto {
    @NotNull(message = "시작일은 필수 입력값입니다.")
    private LocalDate startDate;
    
    @NotNull(message = "종료일은 필수 입력값입니다.")
    private LocalDate endDate;
} 