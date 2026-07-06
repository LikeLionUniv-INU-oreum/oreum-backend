package com.likelion.orum.domain.auth.exception;

import com.likelion.orum.global.exception.code.BaseErrorCode;
import org.springframework.http.HttpStatus;

public enum AuthErrorCode implements BaseErrorCode {
    EMAIL_VERIFICATION_CODE_MISMATCH("AUTH_4001", "인증번호가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_VERIFIED("AUTH_4031", "이메일 인증이 완료되지 않았습니다.", HttpStatus.FORBIDDEN),
    EMAIL_VERIFICATION_NOT_FOUND("AUTH_4041", "전송된 인증번호가 없습니다.", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_REGISTERED("AUTH_4091", "이미 가입된 이메일입니다.", HttpStatus.CONFLICT),
    EMAIL_VERIFICATION_EXPIRED("AUTH_4092", "인증번호가 만료되었습니다.", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus status;

    AuthErrorCode(String code, String message, HttpStatus status) {
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
