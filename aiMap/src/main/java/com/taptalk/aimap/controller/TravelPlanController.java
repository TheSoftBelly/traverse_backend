package com.taptalk.aimap.controller;

import com.taptalk.aimap.dto.travel.TravelPlanSubmitRequest;
import com.taptalk.aimap.entity.Trip;
import com.taptalk.aimap.service.TravelPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 여행 계획 관련 HTTP 요청을 처리하는 컨트롤러
 * 
 * 주요 엔드포인트:
 * - POST /api/travel-plan/submit: 여행 계획 제출
 * - GET /api/travel-plan: 여행 목록 조회
 * - GET /api/travel-plan/{tripId}: 여행 상세 정보 조회
 * - GET /api/travel-plan/{tripId}/schedule: 일정별 장소 조회
 * - GET /api/travel-plan/{tripId}/essentials: 여행 필수품 조회
 * - PUT /api/travel-plan/{tripId}: 여행 정보 수정
 * - PUT /api/travel-plan/{tripId}/schedule: 일정 수정
 * - PUT /api/travel-plan/{tripId}/essentials: 여행 필수품 수정
 * - DELETE /api/travel-plan/{tripId}: 여행 삭제
 * 
 * 보안:
 * - @AuthenticationPrincipal을 사용하여 인증된 사용자 정보 확인
 * - 각 요청마다 사용자 권한 검증
 */
@RestController
@RequestMapping("/api/travel-plan")
@RequiredArgsConstructor
public class TravelPlanController {
    private final TravelPlanService travelPlanService;

    /**
     * 여행 계획을 제출하는 엔드포인트
     * 
     * @param userId 인증된 사용자 ID
     * @param request 여행 계획 제출 요청 데이터
     * @return 저장된 여행 정보
     */
    @PostMapping("/submit")
    public ResponseEntity<Trip> submitTravelPlan(
            @AuthenticationPrincipal Long userId,
            @RequestBody TravelPlanSubmitRequest request) {
        Trip trip = travelPlanService.submitTravelPlan(userId, request);
        return ResponseEntity.ok(trip);
    }
} 