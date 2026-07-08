package com.likelion.orum.domain.todo.repository;

import com.likelion.orum.domain.term.enums.TermType;
import com.likelion.orum.domain.todo.entity.Todo;
import java.util.List;
import java.util.Optional;

import com.likelion.orum.domain.todo.enums.TodoStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // 투두를 조회할 때 category도 같이 가져오도록 (N+1 방지)
    @EntityGraph(attributePaths = "category")
    List<Todo> findAllByTerm_IdOrderByCreatedAtAsc(Long termId);

    @EntityGraph(attributePaths = {
            "category",
            "term",
            "term.userProfile",
            "term.userProfile.user"
    })
    Optional<Todo> findWithDetailById(Long todoId);

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
    List<Integer> findHeightsByJobAndTerm(
            Long jobId,
            Integer year,
            TermType termType,
            TodoStatus completedStatus
    );
}
