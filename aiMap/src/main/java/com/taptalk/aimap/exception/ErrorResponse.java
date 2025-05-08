package com.taptalk.aimap.exception;

import lombok.Builder;
import lombok.Getter;

/**
 * 에러 응답을 위한 DTO 클래스
 */
@Getter
@Builder
public class ErrorResponse {
    private String code;
    private String message;
    private String detail;

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

    public static ErrorResponse of(ErrorCode errorCode, String detail) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .detail(detail)
                .build();
    }
} 