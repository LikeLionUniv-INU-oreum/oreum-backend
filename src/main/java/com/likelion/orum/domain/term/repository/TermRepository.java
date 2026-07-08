package com.likelion.orum.domain.term.repository;

import com.likelion.orum.domain.term.entity.Term;
import com.likelion.orum.domain.term.enums.TermType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermRepository extends JpaRepository<Term, Long> {

    Optional<Term> findByUserProfile_User_IdAndYearAndTermType(Long userId, Integer year, TermType termType);
}
