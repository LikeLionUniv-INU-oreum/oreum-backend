package com.likelion.orum.domain.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StarCardCreateRequestDto(

        @NotBlank(message = "상황을 입력해주세요.")
        @Size(max = 1000, message = "상황은 1000자 이하여야 합니다.")
        String situation,

        @NotBlank(message = "과제를 입력해주세요.")
        @Size(max = 1000, message = "과제는 1000자 이하여야 합니다.")
        String task,

        @NotBlank(message = "행동을 입력해주세요.")
        @Size(max = 1000, message = "행동은 1000자 이하여야 합니다.")
        String action,

        @NotBlank(message = "결과를 입력해주세요.")
        @Size(max = 1000, message = "결과는 1000자 이하여야 합니다.")
        String result
) {
}