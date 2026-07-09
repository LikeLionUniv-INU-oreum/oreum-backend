package com.likelion.orum.domain.term.entity;

import com.likelion.orum.domain.term.enums.TermType;
import com.likelion.orum.domain.user.entity.UserProfile;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "terms",
        uniqueConstraints = {
                // 한 명의 사용자는 동일한 연도와 분기 정보를 중복으로 가질 수 없다.
                @UniqueConstraint(name = "uk_terms_user_profile_year_term_type", columnNames = {"user_profile_id", "year", "term_type"})
        }
)@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Term {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "term_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfile userProfile;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(name = "term_type", nullable = false, length = 30)
    private TermType termType;

    public static Term create(UserProfile userProfile, Integer year, TermType termType) {
        Term term = new Term();
        term.userProfile = userProfile;
        term.year = year;
        term.termType = termType;
        return term;
    }
}
