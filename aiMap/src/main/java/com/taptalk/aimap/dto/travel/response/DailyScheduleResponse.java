package com.taptalk.aimap.dto.travel.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 일정 응답을 위한 DTO 클래스
 */
@Getter
@Builder
public class DailyScheduleResponse {
    private Long scheduleId;
    private Long placeId;
    private String placeName;
    private LocalDate visitDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer visitOrder;
} 