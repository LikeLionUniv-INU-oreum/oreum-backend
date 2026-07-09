package com.likelion.orum.domain.review.repository;

import com.likelion.orum.domain.review.entity.ReviewLike;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    Optional<ReviewLike> findByCourseReview_IdAndUser_Id(Long courseReviewId, Long userId);

    Long countByCourseReview_Id(Long courseReviewId);

    @Query("""
            SELECT rl.courseReview.id
            FROM ReviewLike rl
            WHERE rl.user.id = :userId
              AND rl.courseReview.id IN :reviewIds
            """)
    List<Long> findLikedReviewIds(
            @Param("userId") Long userId,
            @Param("reviewIds") Collection<Long> reviewIds
    );

    @Query("""
            SELECT rl.courseReview.id, COUNT(rl)
            FROM ReviewLike rl
            WHERE rl.courseReview.id IN :reviewIds
            GROUP BY rl.courseReview.id
            """)
    List<Object[]> countLikesByReviewIds(@Param("reviewIds") Collection<Long> reviewIds);
}