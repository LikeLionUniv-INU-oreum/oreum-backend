package com.likelion.orum.domain.review.repository;

import com.likelion.orum.domain.review.entity.ReviewRecommendedGrade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRecommendedGradeRepository extends JpaRepository<ReviewRecommendedGrade, Long> {
}