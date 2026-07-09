package com.likelion.orum.domain.user.dto.request;

import com.likelion.orum.domain.user.enums.AcademicStatus;
import jakarta.validation.constraints.NotNull;

public record OnboardingRequestDto(

        @NotNull(message = "전공 ID를 입력해주세요.")
        Long majorId,

        @NotNull(message = "희망 직무 ID를 입력해주세요.")
        Long jobId,

        @NotNull(message = "학적 상태를 입력해주세요.")
        AcademicStatus academicStatus
) {
}
