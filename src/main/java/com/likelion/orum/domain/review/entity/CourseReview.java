package com.likelion.orum.domain.review.entity;

import com.likelion.orum.domain.common.entity.BaseTimeEntity;
import com.likelion.orum.domain.review.enums.ReviewStatus;
import com.likelion.orum.domain.todo.entity.Todo;
import com.likelion.orum.domain.user.enums.AcademicStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "course_reviews",
        uniqueConstraints = {
                // 하나의 할 일에는 하나의 코스 리뷰만 작성할 수 있다.
                @UniqueConstraint(name = "uk_course_reviews_todo_id", columnNames = "todo_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_review_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    private Todo todo;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Enumerated(EnumType.STRING)
    @Column(name = "ascent_grade", nullable = false, length = 30)
    private AcademicStatus ascentGrade;

    @Column(name = "ascent_semester", nullable = false, length = 30)
    private String ascentSemester;

    @Column(name = "duration", nullable = false, length = 50)
    private String duration;

    @Lob
    @Column(name = "tip", nullable = false)
    private String tip;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_status", nullable = false, length = 30)
    private ReviewStatus reviewStatus;
}
