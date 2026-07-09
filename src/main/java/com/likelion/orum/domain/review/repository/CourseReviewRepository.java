package com.likelion.orum.domain.review.repository;

import com.likelion.orum.domain.review.entity.CourseReview;
import com.likelion.orum.domain.review.enums.RecommendedGrade;
import com.likelion.orum.domain.review.enums.ReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {

    boolean existsByTodo_Id(Long todoId);

    @EntityGraph(attributePaths = {
            "todo",
            "todo.category",
            "todo.term",
            "todo.term.userProfile",
            "todo.term.userProfile.job"
    })
    @Query("""
            SELECT DISTINCT cr
            FROM CourseReview cr
            JOIN cr.todo td
            JOIN td.category c
            JOIN td.term t
            JOIN t.userProfile up
            LEFT JOIN ReviewRecommendedGrade rrg ON rrg.courseReview = cr
            WHERE up.job.id = :jobId
              AND cr.reviewStatus = :reviewStatus
              AND (:categoryId IS NULL OR c.id = :categoryId)
              AND (
                    :grade IS NULL
                    OR rrg.recommendedGrade = :grade
                    OR rrg.recommendedGrade = com.likelion.orum.domain.review.enums.RecommendedGrade.ALL
              )
            ORDER BY cr.createdAt DESC
            """)
    Page<CourseReview> findReviewsOrderByRecent(
            @Param("jobId") Long jobId,
            @Param("categoryId") Long categoryId,
            @Param("grade") RecommendedGrade grade,
            @Param("reviewStatus") ReviewStatus reviewStatus,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {
            "todo",
            "todo.category",
            "todo.term",
            "todo.term.userProfile",
            "todo.term.userProfile.job"
    })
    @Query("""
            SELECT cr
            FROM CourseReview cr
            JOIN cr.todo td
            JOIN td.category c
            JOIN td.term t
            JOIN t.userProfile up
            LEFT JOIN ReviewRecommendedGrade rrg ON rrg.courseReview = cr
            LEFT JOIN ReviewLike rl ON rl.courseReview = cr
            WHERE up.job.id = :jobId
              AND cr.reviewStatus = :reviewStatus
              AND (:categoryId IS NULL OR c.id = :categoryId)
              AND (
                    :grade IS NULL
                    OR rrg.recommendedGrade = :grade
                    OR rrg.recommendedGrade = com.likelion.orum.domain.review.enums.RecommendedGrade.ALL
              )
            GROUP BY cr
            ORDER BY COUNT(rl) DESC, cr.createdAt DESC
            """)
    Page<CourseReview> findReviewsOrderByPopular(
            @Param("jobId") Long jobId,
            @Param("categoryId") Long categoryId,
            @Param("grade") RecommendedGrade grade,
            @Param("reviewStatus") ReviewStatus reviewStatus,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {
            "todo",
            "todo.category",
            "todo.term",
            "todo.term.userProfile",
            "todo.term.userProfile.job"
    })
    Optional<CourseReview> findByIdAndReviewStatus(
            Long courseReviewId,
            ReviewStatus reviewStatus
    );
}