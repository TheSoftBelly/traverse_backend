package com.taptalk.aimap.dto.travel.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 여행 기간 응답을 위한 DTO 클래스
 */
@Getter
@Builder
public class TravelDatesResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private int duration;
} 