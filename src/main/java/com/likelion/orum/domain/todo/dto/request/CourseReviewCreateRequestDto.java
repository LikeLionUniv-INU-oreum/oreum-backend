package com.likelion.orum.domain.todo.dto.request;

import com.likelion.orum.domain.review.enums.RecommendedGrade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record CourseReviewCreateRequestDto(
        @NotNull(message = "만족도를 입력해주세요.")
        @DecimalMin(value = "0.0", message = "만족도는 0점 이상이어야 합니다.")
        @DecimalMax(value = "5.0", message = "만족도는 5점 이하여야 합니다.")
        Double rating,

        @NotBlank(message = "등반 시기를 선택해주세요.")
        String ascentPeriod,

        @NotEmpty(message = "추천 시기를 선택해주세요.")
        List<@NotNull(message = "추천 시기 값이 올바르지 않습니다.") RecommendedGrade> recommendedGrades,

        @NotBlank(message = "소요 기간을 입력해주세요.")
        String duration,

        @NotBlank(message = "TIP을 입력해주세요.")
        String tip,

        @Valid
        StarCardCreateRequestDto starCard
) {
}