package com.likelion.orum.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateAcademicStatusRequestDto(

        @NotBlank(message = "학적 상태를 입력해주세요.")
        String grade
) {
}
