package com.taptalk.aimap.service;

import com.taptalk.aimap.dto.travel.TravelPlanSubmitRequest;
import com.taptalk.aimap.entity.Trip;
import org.springframework.stereotype.Service;

/**
 * 여행 계획 관련 비즈니스 로직을 처리하는 서비스 인터페이스
 * 
 * 주요 기능:
 * - 여행 계획 제출
 * - 여행 목록 조회
 * - 여행 상세 정보 조회
 * - 일정별 장소 조회
 * - 여행 필수품 조회
 * - 여행 정보 수정
 * - 일정 수정
 * - 여행 필수품 수정
 * - 여행 삭제
 */
@Service
public interface TravelPlanService {
    /**
     * 여행 계획을 제출하고 관련된 모든 정보를 저장
     * 
     * @param userId 사용자 ID
     * @param request 여행 계획 제출 요청 데이터
     * @return 저장된 여행 정보
     */
    Trip submitTravelPlan(Long userId, TravelPlanSubmitRequest request);
} 