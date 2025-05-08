package com.taptalk.aimap.dto.travel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 여행 계획 제출 요청을 위한 DTO 클래스
 * 
 * 주요 필드:
 * - destination: 여행지 정보
 * - travelDates: 여행 기간 정보
 * - companionType: 동행자 유형
 * - concepts: 여행 컨셉 리스트
 * - style: 여행 스타일
 * - transportation: 교통수단 정보
 * - accommodations: 숙소 정보 리스트
 * - selectedPlaces: 선택된 장소 정보 리스트
 * - communication: 통신 관련 정보
 * - documents: 여행 서류 정보
 * - insurance: 보험 정보
 * - budget: 예산 정보
 */
@Getter
@Setter
public class TravelPlanSubmitRequest {
    @NotNull(message = "여행지 정보는 필수 입력값입니다.")
    @Valid
    private DestinationDto destination;
    
    @NotNull(message = "여행 기간 정보는 필수 입력값입니다.")
    @Valid
    private TravelDatesDto travelDates;
    
    @NotNull(message = "동행자 유형은 필수 입력값입니다.")
    private String companionType;
    
    @NotEmpty(message = "여행 컨셉은 필수 입력값입니다.")
    private List<String> concepts;
    
    @NotNull(message = "여행 스타일은 필수 입력값입니다.")
    private String style;
    
    @NotNull(message = "교통수단 정보는 필수 입력값입니다.")
    @Valid
    private TransportationDto transportation;
    
    @NotEmpty(message = "숙소 정보는 필수 입력값입니다.")
    @Valid
    private List<AccommodationDto> accommodations;
    
    @NotEmpty(message = "선택된 장소 정보는 필수 입력값입니다.")
    @Valid
    private List<SelectedPlaceDto> selectedPlaces;
    
    @NotNull(message = "통신 관련 정보는 필수 입력값입니다.")
    @Valid
    private CommunicationDto communication;
    
    @NotNull(message = "여행 서류 정보는 필수 입력값입니다.")
    @Valid
    private DocumentsDto documents;
    
    @NotNull(message = "보험 정보는 필수 입력값입니다.")
    @Valid
    private InsuranceDto insurance;
    
    @NotNull(message = "예산 정보는 필수 입력값입니다.")
    @Valid
    private BudgetDto budget;
} 