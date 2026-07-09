package com.likelion.orum.domain.review.dto.response;

public record CourseReviewLikeResponseDto(
        Long courseReviewId,
        Boolean liked,
        Long likeCount
) {
}