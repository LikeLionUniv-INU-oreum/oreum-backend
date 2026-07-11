package com.likelion.orum.domain.review.dto.response;

import com.likelion.orum.domain.review.entity.CourseReview;
import com.likelion.orum.domain.review.enums.Grade;
import com.likelion.orum.domain.review.enums.RecommendedGrade;
import com.likelion.orum.domain.term.enums.TermType;
import java.util.List;

public record CourseReviewDetailResponseDto(
        Long courseReviewId,
        String courseName,
        String writerNickname,
        String categoryName,
        Double rating,
        Grade ascentGrade,
        TermType ascentSemester,
        List<RecommendedGrade> recommendedGrades,
        String duration,
        String tip,
        Long likeCount,
        Boolean liked
) {

    public static CourseReviewDetailResponseDto of(
            CourseReview review,
            List<RecommendedGrade> recommendedGrades,
            Long likeCount,
            Boolean liked
    ) {
        return new CourseReviewDetailResponseDto(
                review.getId(),
                review.getTodo().getCourseName(),
                review.getTodo().getTerm().getUserProfile().getUser().getNickname(),
                review.getTodo().getCategory().getCategoryName(),
                review.getRating(),
                review.getAscentGrade(),
                review.getAscentSemester(),
                recommendedGrades,
                review.getDuration(),
                review.getTip(),
                likeCount,
                liked
        );
    }
}