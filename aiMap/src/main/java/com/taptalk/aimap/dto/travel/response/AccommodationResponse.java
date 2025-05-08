package com.taptalk.aimap.dto.travel.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 숙소 응답을 위한 DTO 클래스
 */
@Getter
@Builder
public class AccommodationResponse {
    private Long accommodationId;
    private LocalDate date;
    private String name;
    private LocalTime checkinTime;
    private LocalTime checkoutTime;
    private String reservationStatus;
} 