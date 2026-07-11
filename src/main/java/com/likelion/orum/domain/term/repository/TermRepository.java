package com.likelion.orum.domain.term.repository;

import com.likelion.orum.domain.term.entity.Term;
import com.likelion.orum.domain.term.enums.TermType;
import java.util.Optional;

import com.likelion.orum.domain.user.entity.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermRepository extends JpaRepository<Term, Long> {

    @EntityGraph(attributePaths = {"userProfile", "userProfile.job"})
    Optional<Term> findByUserProfile_User_IdAndYearAndTermType(Long userId, Integer year, TermType termType);

    Optional<Term> findByUserProfileAndYearAndTermType(UserProfile userProfile, Integer year, TermType termType);
}
