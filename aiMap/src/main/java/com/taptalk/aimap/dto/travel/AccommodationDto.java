package com.taptalk.aimap.dto.travel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 숙소 정보를 전달하기 위한 DTO 클래스
 * 
 * 주요 필드:
 * - date: 체크인 날짜
 * - name: 숙소 이름
 * - checkinTime: 체크인 시간
 * - checkoutTime: 체크아웃 시간
 * - reservationStatus: 예약 상태
 * 
 * 사용처:
 * - 숙소 예약 정보 관리
 * - 체크인/체크아웃 시간 기반 일정 관리
 * - 숙소비 예산 계산
 */
@Getter
@Setter
public class AccommodationDto {
    @NotNull(message = "체크인 날짜는 필수 입력값입니다.")
    private LocalDate date;
    
    @NotBlank(message = "숙소 이름은 필수 입력값입니다.")
    private String name;
    
    @NotNull(message = "체크인 시간은 필수 입력값입니다.")
    private LocalTime checkinTime;
    
    @NotNull(message = "체크아웃 시간은 필수 입력값입니다.")
    private LocalTime checkoutTime;
    
    @NotBlank(message = "예약 상태는 필수 입력값입니다.")
    private String reservationStatus;
} 