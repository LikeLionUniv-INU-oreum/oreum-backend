package com.likelion.orum.global.security.principal;

/**
 * JWT 인증이 완료된 사용자 정보를 담는 객체.
 * 인증이 필요한 API에서 현재 로그인한 사용자의 userId, universityEmail을 꺼낼 때 사용.
 */
public record AuthenticatedUser(
        Long userId,
        String universityEmail
) {
}
