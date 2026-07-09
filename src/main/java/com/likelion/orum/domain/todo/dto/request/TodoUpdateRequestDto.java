package com.likelion.orum.domain.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TodoUpdateRequestDto(
        @NotNull(message = "카테고리를 선택해주세요.")
        Long categoryId,

        @NotBlank(message = "코스명을 입력해주세요.")
        @Size(max = 100, message = "코스명은 100자 이하여야 합니다.")
        String courseName,

        @NotBlank(message = "주차별 계획을 입력해주세요.")
        @Size(max = 1000, message = "주차별 계획은 1000자 이하여야 합니다.")
        String weeklyPlan
) {
}