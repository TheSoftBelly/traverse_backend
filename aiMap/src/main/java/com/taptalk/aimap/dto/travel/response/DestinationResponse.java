package com.taptalk.aimap.dto.travel.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 여행지 응답을 위한 DTO 클래스
 */
@Getter
@Builder
public class DestinationResponse {
    private Long destinationId;
    private String name;
    private String type;
    private String countryCode;
    private String cityId;
    private String areaCode;
    private String contentTypeId;
} 