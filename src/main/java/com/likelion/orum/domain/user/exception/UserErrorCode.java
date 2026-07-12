package com.likelion.orum.domain.user.exception;

import com.likelion.orum.global.exception.code.BaseErrorCode;
import org.springframework.http.HttpStatus;

public enum UserErrorCode implements BaseErrorCode {

    USER_NOT_FOUND("USER_4041", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ONBOARDING_ALREADY_COMPLETED("USER_4091", "이미 온보딩을 완료한 사용자입니다.", HttpStatus.CONFLICT),
    USER_PROFILE_NOT_FOUND("USER_4042", "사용자 프로필을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CURRENT_PASSWORD_MISMATCH("USER_4001", "현재 비밀번호가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_ACADEMIC_STATUS("USER_4002", "학적 상태 값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;

    UserErrorCode(String code, String message, HttpStatus status) {
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
