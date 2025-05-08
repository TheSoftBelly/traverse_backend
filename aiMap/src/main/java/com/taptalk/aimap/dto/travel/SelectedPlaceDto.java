package com.taptalk.aimap.dto.travel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/**
 * 선택된 장소 정보를 전달하기 위한 DTO 클래스
 * 
 * 주요 필드:
 * - id: 장소 고유 식별자
 * - day: 방문 일자 (여행 시작일로부터의 일수)
 * - startTime: 방문 시작 시간
 * - endTime: 방문 종료 시간
 * - order: 방문 순서
 * 
 * 사용처:
 * - 일정별 장소 관리
 * - 이동 경로 최적화
 * - 시간대별 일정 관리
 */
@Getter
@Setter
public class SelectedPlaceDto {
    @NotBlank(message = "장소 ID는 필수 입력값입니다.")
    private String id;
    
    @NotNull(message = "방문 일자는 필수 입력값입니다.")
    @Positive(message = "방문 일자는 0보다 커야 합니다.")
    private Integer day;
    
    @NotNull(message = "시작 시간은 필수 입력값입니다.")
    private LocalTime startTime;
    
    @NotNull(message = "종료 시간은 필수 입력값입니다.")
    private LocalTime endTime;
    
    @NotNull(message = "순서는 필수 입력값입니다.")
    @Positive(message = "순서는 0보다 커야 합니다.")
    private Integer order;
} 