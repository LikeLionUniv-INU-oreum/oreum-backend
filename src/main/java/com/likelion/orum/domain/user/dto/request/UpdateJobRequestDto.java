package com.likelion.orum.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateJobRequestDto(

        @NotBlank(message = "희망 직무를 입력해주세요.")
        String jobName
) {
}
