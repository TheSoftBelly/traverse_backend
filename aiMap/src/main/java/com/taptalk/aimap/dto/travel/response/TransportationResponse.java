package com.taptalk.aimap.dto.travel.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 교통수단 응답을 위한 DTO 클래스
 */
@Getter
@Builder
public class TransportationResponse {
    private String arrivalTransport;
    private String localTransport;
    private boolean hasPass;
    private boolean rentCar;
    private boolean hasInternationalLicense;
} 