package com.likelion.orum.domain.auth.dto.request;

public record EmailVerificationConfirmRequestDto (
        String universityEmail,
        String verificationCode
) {
}
