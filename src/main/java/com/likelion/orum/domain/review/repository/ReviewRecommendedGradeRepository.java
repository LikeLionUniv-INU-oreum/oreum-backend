package com.likelion.orum.domain.review.repository;

import com.likelion.orum.domain.review.entity.CourseReview;
import com.likelion.orum.domain.review.entity.ReviewRecommendedGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ReviewRecommendedGradeRepository extends JpaRepository<ReviewRecommendedGrade, Long> {

    List<ReviewRecommendedGrade> findAllByCourseReviewIn(Collection<CourseReview> courseReviews);

    List<ReviewRecommendedGrade> findAllByCourseReview(CourseReview courseReview);
}