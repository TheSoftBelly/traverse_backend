package com.taptalk.aimap.dto.travel;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 여행지 정보를 전달하기 위한 DTO 클래스
 * 
 * 주요 필드:
 * - id: 여행지 고유 식별자
 * - name: 여행지 이름
 * - type: 여행지 타입 ("domestic" 또는 "international")
 * - countryCode: 해외 여행 시 국가 코드
 * - cityId: 해외 여행 시 도시 ID
 * - areaCode: 국내 여행 시 지역 코드
 * - contentTypeId: 국내 여행 시 콘텐츠 타입 ID
 */
@Getter
@Setter
public class DestinationDto {
    private String id;
    
    @NotBlank(message = "여행지 이름은 필수 입력값입니다.")
    private String name;
    
    @NotBlank(message = "여행지 타입은 필수 입력값입니다.")
    private String type; // "domestic" 또는 "international"
    
    private String countryCode; // 해외 여행 시 필요
    private String cityId; // 해외 여행 시 필요
    private String areaCode; // 국내 여행 시 필요
    private String contentTypeId; // 국내 여행 시 필요
} 