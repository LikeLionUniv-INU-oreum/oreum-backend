package com.likelion.orum.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.orum.domain.auth.exception.AuthErrorCode;
import com.likelion.orum.global.exception.code.BaseErrorCode;
import com.likelion.orum.global.exception.code.SecurityErrorCode;
import com.likelion.orum.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/** 인증되지 않은 사용자가 인증이 필요한 API에 접근했을 때 실행되는 핸들러. */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    // 인증 정보가 없는 요청에 대해 401 응답 반환
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        BaseErrorCode errorCode = SecurityErrorCode.AUTHENTICATION_REQUIRED;

        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResponse<Void> body = ApiResponse.failure(
                errorCode.getCode(),
                errorCode.getMessage(),
                null
        );

        objectMapper.writeValue(response.getWriter(), body);
    }
}
