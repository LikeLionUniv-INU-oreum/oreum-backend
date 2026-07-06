package com.likelion.orum.domain.auth.dto.request;

public record SignupRequestDto (
        String universityEmail,
        String password,
        String nickname
) {
}
