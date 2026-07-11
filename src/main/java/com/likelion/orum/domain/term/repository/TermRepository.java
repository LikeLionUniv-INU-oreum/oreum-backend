package com.likelion.orum.domain.term.repository;

import com.likelion.orum.domain.term.entity.Term;
import com.likelion.orum.domain.term.enums.TermType;

import java.util.List;
import java.util.Optional;

import com.likelion.orum.domain.user.entity.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TermRepository extends JpaRepository<Term, Long> {

    @EntityGraph(attributePaths = {"userProfile", "userProfile.job"})
    Optional<Term> findByUserProfile_User_IdAndYearAndTermType(Long userId, Integer year, TermType termType);

    Optional<Term> findByUserProfileAndYearAndTermType(UserProfile userProfile, Integer year, TermType termType);

    @Query("""
            SELECT t
            FROM Term t
            WHERE t.userProfile.user.id = :userId
            ORDER BY t.year DESC,
                     CASE
                         WHEN t.termType = com.likelion.orum.domain.term.enums.TermType.SECOND_HALF THEN 2
                         WHEN t.termType = com.likelion.orum.domain.term.enums.TermType.FIRST_HALF THEN 1
                         ELSE 0
                     END DESC
            """)
    List<Term> findAllByUserIdOrderByYearDescAndTermTypeDesc(
            @Param("userId") Long userId
    );
}
