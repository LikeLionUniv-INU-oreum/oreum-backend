package com.likelion.orum.domain.review.exception;

import com.likelion.orum.global.exception.code.BaseErrorCode;
import org.springframework.http.HttpStatus;

public enum ReviewErrorCode implements BaseErrorCode {

    REVIEW_NOT_FOUND("REVIEW_4041", "리뷰를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_REVIEW_FILTER("REVIEW_4001", "리뷰 조회 조건이 올바르지 않습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ReviewErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}