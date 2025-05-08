package com.taptalk.aimap.dto.travel;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 통신 관련 정보를 전달하기 위한 DTO 클래스
 * 
 * 주요 필드:
 * - hasUsim: USIM 보유 여부
 * - hasPocketWifi: 포켓와이파이 보유 여부
 * 
 * 사용처:
 * - 해외 여행 시 통신 수단 확인
 * - 통신비 예산 계산
 * - 현지 통신 서비스 추천
 */
@Getter
@Setter
public class CommunicationDto {
    @NotNull(message = "USIM 보유 여부는 필수 입력값입니다.")
    private Boolean hasUsim;
    
    @NotNull(message = "포켓와이파이 보유 여부는 필수 입력값입니다.")
    private Boolean hasPocketWifi;
} 