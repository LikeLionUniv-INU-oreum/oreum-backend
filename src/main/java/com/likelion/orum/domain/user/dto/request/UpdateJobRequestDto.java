package com.likelion.orum.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateJobRequestDto(

        @NotNull(message = "희망 직무 ID를 입력해주세요.")
        Long jobId
) {
}
