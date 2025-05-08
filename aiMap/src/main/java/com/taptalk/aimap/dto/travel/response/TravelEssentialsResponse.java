package com.taptalk.aimap.dto.travel.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 여행 필수품 응답을 위한 DTO 클래스
 */
@Getter
@Builder
public class TravelEssentialsResponse {
    private Long essentialsId;
    private boolean hasPassport;
    private boolean hasVisa;
    private boolean hasTravelInsurance;
    private boolean hasUsim;
    private boolean hasPocketWifi;
} 