package com.likelion.orum.domain.todo.dto.request;

import com.likelion.orum.domain.review.enums.Grade;
import com.likelion.orum.domain.review.enums.RecommendedGrade;
import com.likelion.orum.domain.term.enums.TermType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record CourseReviewCreateRequestDto(
        @NotNull(message = "만족도를 입력해주세요.")
        Double rating,

        @NotNull(message = "등반 학년을 선택해주세요.")
        Grade ascentGrade,

        @NotNull(message = "등반 학기를 선택해주세요.")
        TermType ascentSemester,

        @NotEmpty(message = "추천 시기를 선택해주세요.")
        List<RecommendedGrade> recommendedGrades,

        @NotBlank(message = "소요 기간을 입력해주세요.")
        String duration,

        @NotBlank(message = "TIP을 입력해주세요.")
        String tip,

        @Valid
        StarCardCreateRequestDto starCard
) {
}