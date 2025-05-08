package com.taptalk.aimap.dto.travel;

import com.taptalk.aimap.entity.Trip;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 교통수단 정보를 전달하기 위한 DTO 클래스
 * 
 * 주요 필드:
 * - arrivalTransport: 도착 교통수단 (비행기, 기차, 버스 등)
 * - localTransport: 현지 교통수단 (지하철, 버스, 택시 등)
 * - hasPass: 교통패스 보유 여부
 * - rentCar: 렌트카 사용 여부
 * - hasInternationalLicense: 국제면허증 보유 여부
 * 
 * 사용처:
 * - 여행 계획 생성 시 교통수단 설정
 * - 교통비 예산 계산
 * - 이동 경로 최적화
 */
@Getter
@Setter
public class TransportationDto {
    @NotNull(message = "도착 교통수단은 필수 입력값입니다.")
    private Trip.ArrivalTransport arrivalTransport;
    
    @NotNull(message = "현지 교통수단은 필수 입력값입니다.")
    private Trip.LocalTransport localTransport;
    
    private Boolean hasPass;
    private Boolean rentCar;
    private Boolean hasInternationalLicense;
} 