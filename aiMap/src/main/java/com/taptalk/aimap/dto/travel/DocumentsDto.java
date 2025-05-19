package com.taptalk.aimap.dto.travel;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 여행 서류 정보를 전달하기 위한 DTO 클래스
 * 
 * 주요 필드:
 * - hasPassport: 여권 보유 여부
 * - hasVisa: 비자 보유 여부
 * 
 * 사용처:
 * - 해외 여행 시 필수 서류 확인
 * - 비자 발급 필요 여부 확인
 * - 여행 준비 체크리스트 관리
 */
@Getter
@Setter
public class DocumentsDto {
    @NotNull(message = "여권 보유 여부는 필수 입력값입니다.")
    private Boolean hasPassport;
    
    @NotNull(message = "비자 보유 여부는 필수 입력값입니다.")
    private Boolean hasVisa;
} 