package com.likelion.orum.domain.auth.dto.response;

import com.likelion.orum.domain.user.entity.User;

public record LoginResponseDto(
        String accessToken,
        Long userId,
        String universityEmail,
        String nickname,
        Boolean onboardingCompleted
) {

    public static LoginResponseDto of(String accessToken, User user) {
        return new LoginResponseDto(
                accessToken,
                user.getId(),
                user.getUniversityEmail(),
                user.getNickname(),
                user.getOnboardingCompleted()
        );
    }
}