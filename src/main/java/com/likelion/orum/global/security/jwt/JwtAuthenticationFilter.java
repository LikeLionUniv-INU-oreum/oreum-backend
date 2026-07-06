package com.likelion.orum.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.orum.global.exception.GeneralException;
import com.likelion.orum.global.exception.code.BaseErrorCode;
import com.likelion.orum.global.response.ApiResponse;
import com.likelion.orum.global.security.principal.AuthenticatedUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 매 요청마다 JWT 인증을 처리하는 필터.
 * Authorization 헤더에서 Bearer 토큰을 꺼내 검증.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    // 매 요청마다 JWT 인증을 처리
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = resolveToken(request);

        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 토큰 검증 및 사용자 정보 추출
            AuthenticatedUser authenticatedUser = jwtTokenProvider.getAuthenticatedUser(token);

            // Spring Security가 이해할 수 있는 Authentication 객체 생성
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            authenticatedUser,
                            null,
                            Collections.emptyList()
                    );

            // 현재 요청의 인증 정보를 SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (GeneralException e) {
            SecurityContextHolder.clearContext();
            writeErrorResponse(response, e.getErrorCode());
        }
    }

    // Authorization 헤더에서 Bearer 토큰 값을 추출
    private String resolveToken(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authorization) && authorization.startsWith(BEARER_PREFIX)) {
            return authorization.substring(BEARER_PREFIX.length());
        }

        return null;
    }

    // 필터 내부에서 발생한 JWT 예외 처리 (ApiResponse 형식)
    private void writeErrorResponse(HttpServletResponse response, BaseErrorCode errorCode) throws IOException {
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
