package com.likelion.orum.domain.term.dto.request;

import com.likelion.orum.domain.term.enums.TermType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TermDashboardRequestDto(

        @NotNull(message = "연도를 입력해주세요.")
        @Min(value = 2000, message = "연도는 2000년 이상이어야 합니다.")
        @Max(value = 2100, message = "연도는 2100년 이하여야 합니다.")
        Integer year,

        @NotNull(message = "학기를 선택해주세요.")
        TermType termType
) {
}
