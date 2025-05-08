package com.taptalk.aimap.dto.travel.response;

import com.taptalk.aimap.dto.travel.response.DestinationResponse;
import com.taptalk.aimap.dto.travel.response.TravelDatesResponse;
import com.taptalk.aimap.dto.travel.response.TransportationResponse;
import com.taptalk.aimap.dto.travel.response.AccommodationResponse;
import com.taptalk.aimap.dto.travel.response.DailyScheduleResponse;
import com.taptalk.aimap.dto.travel.response.TravelEssentialsResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 여행 계획 응답을 위한 DTO 클래스
 */
@Getter
@Builder
public class TravelPlanResponse {
    private Long tripId;
    private DestinationResponse destination;
    private TravelDatesResponse travelDates;
    private String companionType;
    private List<String> concepts;
    private String style;
    private TransportationResponse transportation;
    private List<AccommodationResponse> accommodations;
    private List<DailyScheduleResponse> dailySchedules;
    private TravelEssentialsResponse travelEssentials;
    private Double budget;
} 