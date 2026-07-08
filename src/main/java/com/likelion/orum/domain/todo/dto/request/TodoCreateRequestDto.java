package com.likelion.orum.domain.todo.dto.request;

import com.likelion.orum.domain.term.enums.TermType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TodoCreateRequestDto(

        @NotNull(message = "연도를 입력해주세요.")
        @Min(value = 2000, message = "연도는 2000년 이상이어야 합니다.")
        @Max(value = 2100, message = "연도는 2100년 이하여야 합니다.")
        Integer year,

        @NotNull(message = "학기를 선택해주세요.")
        TermType termType,

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
