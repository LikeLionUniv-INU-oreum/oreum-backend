package com.likelion.orum.domain.user.dto.request;

import com.likelion.orum.domain.user.enums.AcademicStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateAcademicStatusRequestDto(

        @NotNull(message = "학적 상태를 입력해주세요.")
        AcademicStatus academicStatus
) {
}
