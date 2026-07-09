package com.likelion.orum.domain.review.entity;

import com.likelion.orum.domain.review.enums.RecommendedGrade;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "review_recommended_grades",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_review_recommended_grade",
                        columnNames = {"course_review_id", "recommended_grade"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewRecommendedGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_recommended_grade_id")
    private Long reviewRecommendedGradeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_review_id", nullable = false)
    private CourseReview courseReview;

    @Enumerated(EnumType.STRING)
    @Column(name = "recommended_grade", nullable = false, length = 30)
    private RecommendedGrade recommendedGrade;

    public static ReviewRecommendedGrade create(
            CourseReview courseReview,
            RecommendedGrade recommendedGrade
    ) {
        ReviewRecommendedGrade reviewRecommendedGrade = new ReviewRecommendedGrade();
        reviewRecommendedGrade.courseReview = courseReview;
        reviewRecommendedGrade.recommendedGrade = recommendedGrade;
        return reviewRecommendedGrade;
    }
}