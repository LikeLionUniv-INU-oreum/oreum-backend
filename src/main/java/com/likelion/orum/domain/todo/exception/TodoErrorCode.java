package com.likelion.orum.domain.todo.exception;

import com.likelion.orum.global.exception.code.BaseErrorCode;
import org.springframework.http.HttpStatus;

public enum TodoErrorCode implements BaseErrorCode {

    USER_PROFILE_NOT_FOUND("TODO_4041", "사용자 프로필을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND("TODO_4042", "카테고리를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;

    TodoErrorCode(String code, String message, HttpStatus status) {
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
