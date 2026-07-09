package com.likelion.orum.domain.review.dto.response;

import java.util.List;

public record CourseReviewListResponseDto(
        List<CourseReviewSummaryResponseDto> reviews,
        Integer page,
        Integer size,
        Long totalElements,
        Integer totalPages,
        Boolean hasNext
) {
}