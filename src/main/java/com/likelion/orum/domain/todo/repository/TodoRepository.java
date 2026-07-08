package com.likelion.orum.domain.todo.repository;

import com.likelion.orum.domain.term.enums.TermType;
import com.likelion.orum.domain.todo.entity.Todo;
import java.util.List;
import java.util.Optional;

import com.likelion.orum.domain.todo.enums.TodoStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // 투두를 조회할 때 category도 같이 가져오도록 (N+1 방지)
    @EntityGraph(attributePaths = "category")
    List<Todo> findAllByTerm_IdOrderByCreatedAtAsc(Long termId);

    // 할 일 상세 조회 시 todoId + userId 조건으로 조회
    @EntityGraph(attributePaths = {
            "category",
            "term",
            "term.userProfile",
            "term.userProfile.user"
    })
    @Query("""
        SELECT t
        FROM Todo t
        WHERE t.id = :todoId
          AND t.term.userProfile.user.id = :userId
        """)
    Optional<Todo> findWithDetailByIdAndUserId(
            @Param("todoId") Long todoId,
            @Param("userId") Long userId
    );

    // 해당 직무-분기 별 전체 사용자 조회 JPQL
    @Query("""
            SELECT COALESCE(SUM(c.score), 0)
            FROM UserProfile up
            LEFT JOIN Term t
                ON t.userProfile = up
                AND t.year = :year
                AND t.termType = :termType
            LEFT JOIN Todo td
                ON td.term = t
                AND td.todoStatus = :completedStatus
            LEFT JOIN Category c
                ON c = td.category
            WHERE up.job.id = :jobId
            GROUP BY up.id
            """)
    List<Long> findHeightsByJobAndTerm(
            @Param("jobId") Long jobId,
            @Param("year") Integer year,
            @Param("termType") TermType termType,
            @Param("completedStatus") TodoStatus completedStatus
    );
}
