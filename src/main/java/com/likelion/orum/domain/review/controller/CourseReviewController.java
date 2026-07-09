package com.likelion.orum.domain.review.controller;

import com.likelion.orum.domain.review.dto.response.CourseReviewDetailResponseDto;
import com.likelion.orum.domain.review.dto.response.CourseReviewLikeResponseDto;
import com.likelion.orum.domain.review.dto.response.CourseReviewListResponseDto;
import com.likelion.orum.domain.review.enums.ReviewSort;
import com.likelion.orum.domain.review.service.CourseReviewService;
import com.likelion.orum.global.exception.GeneralException;
import com.likelion.orum.global.exception.code.SecurityErrorCode;
import com.likelion.orum.global.response.ApiResponse;
import com.likelion.orum.global.security.principal.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course-reviews")
public class CourseReviewController {

    private final CourseReviewService courseReviewService;

    @GetMapping
    public ResponseEntity<ApiResponse<CourseReviewListResponseDto>> getCourseReviews(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @RequestParam Long jobId,
            @RequestParam(defaultValue = "ALL") String grade,
            @RequestParam(defaultValue = "ALL") String categoryId,
            @RequestParam(defaultValue = "POPULAR") ReviewSort sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (authenticatedUser == null) {
            throw new GeneralException(SecurityErrorCode.AUTHENTICATION_REQUIRED);
        }

        CourseReviewListResponseDto response = courseReviewService.getCourseReviews(
                authenticatedUser.userId(),
                jobId,
                grade,
                categoryId,
                sort,
                page,
                size
        );

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{courseReviewId}")
    public ResponseEntity<ApiResponse<CourseReviewDetailResponseDto>> getCourseReviewDetail(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long courseReviewId
    ) {
        if (authenticatedUser == null) {
            throw new GeneralException(SecurityErrorCode.AUTHENTICATION_REQUIRED);
        }

        CourseReviewDetailResponseDto response = courseReviewService.getCourseReviewDetail(authenticatedUser.userId(), courseReviewId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{courseReviewId}/like")
    public ResponseEntity<ApiResponse<CourseReviewLikeResponseDto>> toggleLike(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long courseReviewId
    ) {
        if (authenticatedUser == null) {
            throw new GeneralException(SecurityErrorCode.AUTHENTICATION_REQUIRED);
        }

        CourseReviewLikeResponseDto response = courseReviewService.toggleLike(authenticatedUser.userId(), courseReviewId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}