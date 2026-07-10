package com.likelion.orum.domain.todo.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TodoUpdateRequestDto(
        @Positive(message = "카테고리 ID는 양수여야 합니다.")
        Long categoryId,

        @Pattern(regexp = ".*\\S.*", message = "코스명을 입력해주세요.")
        @Size(max = 100, message = "코스명은 100자 이하여야 합니다.")
        String courseName,

        @Pattern(regexp = ".*\\S.*", message = "주차별 계획을 입력해주세요.")
        @Size(max = 1000, message = "주차별 계획은 1000자 이하여야 합니다.")
        String weeklyPlan
) {
}