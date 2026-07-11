package com.likelion.orum.domain.term.repository;

import com.likelion.orum.domain.term.entity.Term;
import com.likelion.orum.domain.term.enums.TermType;
import com.likelion.orum.domain.todo.enums.TodoStatus;

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

    // 마이페이지 - 완등한 분기(산) 개수
    // "완등" 기준: 해당 분기에 할 일이 1개 이상 있고, 그 중 진행중(IN_PROGRESS)인 할 일이 하나도 없는 경우
    @Query("""
            SELECT COUNT(t)
            FROM Term t
            WHERE t.userProfile.user.id = :userId
              AND EXISTS (SELECT 1 FROM Todo td WHERE td.term = t)
              AND NOT EXISTS (SELECT 1 FROM Todo td2 WHERE td2.term = t AND td2.todoStatus = :inProgressStatus)
            """)
    Long countCompletedTermsByUserId(
            @Param("userId") Long userId,
            @Param("inProgressStatus") TodoStatus inProgressStatus
    );
}
