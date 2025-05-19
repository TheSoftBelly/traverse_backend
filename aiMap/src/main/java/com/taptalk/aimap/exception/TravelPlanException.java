package com.taptalk.aimap.exception;

import lombok.Getter;

/**
 * 여행 계획 관련 예외를 처리하는 클래스
 */
@Getter
public class TravelPlanException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public TravelPlanException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public TravelPlanException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
} 