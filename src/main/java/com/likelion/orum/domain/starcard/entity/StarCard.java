package com.likelion.orum.domain.starcard.entity;

import com.likelion.orum.domain.common.entity.BaseTimeEntity;
import com.likelion.orum.domain.review.entity.CourseReview;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
        name = "star_cards",
        uniqueConstraints = {
                // 하나의 코스 리뷰에는 하나의 STAR 카드만 연결된다.
                @UniqueConstraint(name = "uk_star_cards_course_review_id", columnNames = "course_review_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StarCard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "star_card_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_review_id", nullable = false)
    private CourseReview courseReview;

    @Lob
    @Column(name = "situation", nullable = false)
    private String situation;

    @Lob
    @Column(name = "task", nullable = false)
    private String task;

    @Lob
    @Column(name = "action", nullable = false)
    private String action;

    @Lob
    @Column(name = "result", nullable = false)
    private String result;

    public static StarCard create(
            CourseReview courseReview,
            String situation,
            String task,
            String action,
            String result
    ) {
        StarCard starCard = new StarCard();
        starCard.courseReview = courseReview;
        starCard.situation = situation;
        starCard.task = task;
        starCard.action = action;
        starCard.result = result;
        return starCard;
    }
}
