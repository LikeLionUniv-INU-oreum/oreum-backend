package com.likelion.orum.domain.review.service;

import com.likelion.orum.domain.review.dto.response.*;
import com.likelion.orum.domain.review.entity.CourseReview;
import com.likelion.orum.domain.review.entity.ReviewLike;
import com.likelion.orum.domain.review.entity.ReviewRecommendedGrade;
import com.likelion.orum.domain.review.enums.RecommendedGrade;
import com.likelion.orum.domain.review.enums.ReviewSort;
import com.likelion.orum.domain.review.enums.ReviewStatus;
import com.likelion.orum.domain.review.exception.ReviewErrorCode;
import com.likelion.orum.domain.review.repository.CourseReviewRepository;
import com.likelion.orum.domain.review.repository.ReviewLikeRepository;
import com.likelion.orum.domain.review.repository.ReviewRecommendedGradeRepository;
import com.likelion.orum.domain.user.entity.User;
import com.likelion.orum.domain.user.repository.UserRepository;
import com.likelion.orum.global.exception.GeneralException;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseReviewService {

    private static final String ALL = "ALL";

    private final CourseReviewRepository courseReviewRepository;
    private final ReviewRecommendedGradeRepository reviewRecommendedGradeRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public CourseReviewListResponseDto getCourseReviews(
            Long userId,
            Long jobId,
            String grade,
            String categoryId,
            ReviewSort sort,
            int page,
            int size
    ) {
        RecommendedGrade parsedGrade = parseGrade(grade);
        Long parsedCategoryId = parseCategoryId(categoryId);

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<CourseReview> reviewPage = sort == ReviewSort.POPULAR
                ? courseReviewRepository.findReviewsOrderByPopular(
                jobId,
                parsedCategoryId,
                parsedGrade,
                ReviewStatus.PUBLIC,
                pageRequest
        )
                : courseReviewRepository.findReviewsOrderByRecent(
                jobId,
                parsedCategoryId,
                parsedGrade,
                ReviewStatus.PUBLIC,
                pageRequest
        );

        List<CourseReview> reviews = reviewPage.getContent();

        Map<Long, List<RecommendedGrade>> recommendedGradeMap = getRecommendedGradeMap(reviews);
        Map<Long, Long> likeCountMap = getLikeCountMap(reviews);
        Set<Long> likedReviewIds = getLikedReviewIds(userId, reviews);

        List<CourseReviewSummaryResponseDto> reviewResponses = reviews.stream()
                .map(review -> CourseReviewSummaryResponseDto.of(
                        review,
                        recommendedGradeMap.getOrDefault(review.getId(), List.of()),
                        likeCountMap.getOrDefault(review.getId(), 0L),
                        likedReviewIds.contains(review.getId())
                ))
                .toList();

        return new CourseReviewListResponseDto(
                reviewResponses,
                reviewPage.getNumber(),
                reviewPage.getSize(),
                reviewPage.getTotalElements(),
                reviewPage.getTotalPages(),
                reviewPage.hasNext()
        );
    }

    @Transactional(readOnly = true)
    public CourseReviewDetailResponseDto getCourseReviewDetail(Long userId, Long courseReviewId) {
        CourseReview review = courseReviewRepository
                .findByIdAndReviewStatus(courseReviewId, ReviewStatus.PUBLIC)
                .orElseThrow(() -> new GeneralException(ReviewErrorCode.REVIEW_NOT_FOUND));

        List<RecommendedGrade> recommendedGrades = reviewRecommendedGradeRepository
                .findAllByCourseReview(review)
                .stream()
                .map(ReviewRecommendedGrade::getRecommendedGrade)
                .toList();

        Long likeCount = reviewLikeRepository.countByCourseReview_Id(courseReviewId);

        boolean liked = reviewLikeRepository
                .findByCourseReview_IdAndUser_Id(courseReviewId, userId)
                .isPresent();

        return CourseReviewDetailResponseDto.of(
                review,
                recommendedGrades,
                likeCount,
                liked
        );
    }

    @Transactional
    public CourseReviewLikeResponseDto toggleLike(Long userId, Long courseReviewId) {
        CourseReview review = courseReviewRepository
                .findByIdAndReviewStatus(courseReviewId, ReviewStatus.PUBLIC)
                .orElseThrow(() -> new GeneralException(ReviewErrorCode.REVIEW_NOT_FOUND));

        Optional<ReviewLike> reviewLike = reviewLikeRepository
                .findByCourseReview_IdAndUser_Id(courseReviewId, userId);

        boolean liked;

        if (reviewLike.isPresent()) {
            reviewLikeRepository.delete(reviewLike.get());
            liked = false;
        }
        else {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new GeneralException(ReviewErrorCode.REVIEW_NOT_FOUND));

            reviewLikeRepository.save(ReviewLike.create(review, user));
            liked = true;
        }

        Long likeCount = reviewLikeRepository.countByCourseReview_Id(courseReviewId);

        return new CourseReviewLikeResponseDto(
                courseReviewId,
                liked,
                likeCount
        );
    }

    private RecommendedGrade parseGrade(String grade) {
        if (grade == null || ALL.equalsIgnoreCase(grade)) {
            return null;
        }

        try {
            return RecommendedGrade.valueOf(grade);
        } catch (IllegalArgumentException e) {
            throw new GeneralException(ReviewErrorCode.INVALID_REVIEW_FILTER);
        }
    }

    private Long parseCategoryId(String categoryId) {
        if (categoryId == null || ALL.equalsIgnoreCase(categoryId)) {
            return null;
        }

        try {
            return Long.valueOf(categoryId);
        } catch (NumberFormatException e) {
            throw new GeneralException(ReviewErrorCode.INVALID_REVIEW_FILTER);
        }
    }

    private Map<Long, List<RecommendedGrade>> getRecommendedGradeMap(List<CourseReview> reviews) {
        if (reviews.isEmpty()) {
            return Map.of();
        }

        return reviewRecommendedGradeRepository.findAllByCourseReviewIn(reviews)
                .stream()
                .collect(Collectors.groupingBy(
                        recommendedGrade -> recommendedGrade.getCourseReview().getId(),
                        Collectors.mapping(
                                ReviewRecommendedGrade::getRecommendedGrade,
                                Collectors.toList()
                        )
                ));
    }

    private Map<Long, Long> getLikeCountMap(List<CourseReview> reviews) {
        if (reviews.isEmpty()) {
            return Map.of();
        }

        List<Long> reviewIds = reviews.stream()
                .map(CourseReview::getId)
                .toList();

        return reviewLikeRepository.countLikesByReviewIds(reviewIds)
                .stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Long) row[1]
                ));
    }

    private Set<Long> getLikedReviewIds(Long userId, List<CourseReview> reviews) {
        if (reviews.isEmpty()) {
            return Set.of();
        }

        List<Long> reviewIds = reviews.stream()
                .map(CourseReview::getId)
                .toList();

        return new HashSet<>(reviewLikeRepository.findLikedReviewIds(userId, reviewIds));
    }
}