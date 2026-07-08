package com.likelion.orum.domain.todo.repository;

import com.likelion.orum.domain.term.enums.TermType;
import com.likelion.orum.domain.todo.entity.Todo;
import java.util.List;

import com.likelion.orum.domain.todo.enums.TodoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findAllByTerm_IdOrderByCreatedAtAsc(Long termId);

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
