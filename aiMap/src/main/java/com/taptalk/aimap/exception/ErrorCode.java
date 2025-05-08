package com.taptalk.aimap.exception;

import lombok.Getter;

/**
 * 에러 코드를 정의하는 열거형
 */
@Getter
public enum ErrorCode {
    // 여행 계획 관련 에러
    TRAVEL_PLAN_NOT_FOUND("여행 계획을 찾을 수 없습니다."),
    INVALID_TRAVEL_DATES("여행 기간이 유효하지 않습니다."),
    INVALID_DESTINATION("여행지 정보가 유효하지 않습니다."),
    INVALID_ACCOMMODATION("숙소 정보가 유효하지 않습니다."),
    INVALID_SCHEDULE("일정 정보가 유효하지 않습니다."),
    
    // 사용자 관련 에러
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    UNAUTHORIZED_ACCESS("접근 권한이 없습니다."),
    
    // 시스템 에러
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다."),
    INVALID_REQUEST("잘못된 요청입니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
} 