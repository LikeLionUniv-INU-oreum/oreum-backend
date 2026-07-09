package com.likelion.orum.domain.review.repository;

import com.likelion.orum.domain.review.entity.CourseReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {

    boolean existsByTodo_Id(Long todoId);
}