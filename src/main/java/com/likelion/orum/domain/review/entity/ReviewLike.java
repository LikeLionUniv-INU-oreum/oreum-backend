package com.likelion.orum.domain.review.entity;

import com.likelion.orum.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "review_likes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_review_like_user",
                        columnNames = {"course_review_id", "user_id"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_like_id")
    private Long reviewLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_review_id", nullable = false)
    private CourseReview courseReview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static ReviewLike create(CourseReview courseReview, User user) {
        ReviewLike reviewLike = new ReviewLike();
        reviewLike.courseReview = courseReview;
        reviewLike.user = user;
        return reviewLike;
    }
}