package com.likelion.orum.domain.todo.repository;

import com.likelion.orum.domain.term.enums.TermType;
import com.likelion.orum.domain.todo.entity.Todo;
import java.util.List;
import java.util.Optional;

import com.likelion.orum.domain.todo.enums.TodoStatus;
import org.springframework.data.domain.Pageable;
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

    // 전체 누적 기준 - 유저별 총 점수 조회 JPQL (고도 계산(m))
    @Query("""
        SELECT COALESCE(SUM(c.score), 0)
        FROM UserProfile up
        LEFT JOIN Term t
            ON t.userProfile = up
        LEFT JOIN Todo td
            ON td.term = t
            AND td.todoStatus = :completedStatus
        LEFT JOIN Category c
            ON c = td.category
        WHERE up.user.id = :userId
        """)
    Long findTotalHeightByUserId(
            @Param("userId") Long userId,
            @Param("completedStatus") TodoStatus completedStatus
    );

    // 전체 누적 기준 - 직무별 총 점수 조회 JPQL (상위 계산(%))
    @Query("""
        SELECT COALESCE(SUM(c.score), 0)
        FROM UserProfile up
        LEFT JOIN Term t
            ON t.userProfile = up
        LEFT JOIN Todo td
            ON td.term = t
            AND td.todoStatus = :completedStatus
        LEFT JOIN Category c
            ON c = td.category
        WHERE up.job.id = :jobId
        GROUP BY up.id
        """)
    List<Long> findTotalHeightsByJobId(
            @Param("jobId") Long jobId,
            @Param("completedStatus") TodoStatus completedStatus
    );

    // 직무별 가장 최근 이수한 할 일 조회
    @Query("""
        SELECT td.courseName
        FROM Todo td
        JOIN td.term t
        JOIN t.userProfile up
        WHERE up.job.id = :jobId
          AND up.user.id <> :userId
          AND td.todoStatus = :completedStatus
        ORDER BY td.completedAt DESC, td.createdAt DESC
        """)
    List<String> findRecentCompletedCourseNamesByJobId(
            @Param("jobId") Long jobId,
            @Param("userId") Long userId,
            @Param("completedStatus") TodoStatus completedStatus,
            Pageable pageable
    );

    // 마이페이지 - 누적 완료한 할 일(코스) 개수 (깃발 개수)
    Long countAllByTerm_UserProfile_User_IdAndTodoStatus(Long userId, TodoStatus todoStatus);

    // 마이페이지 - 완료한 할 일 목록 (카테고리별 통계용, category 같이 조회)
    @EntityGraph(attributePaths = "category")
    List<Todo> findAllByTerm_UserProfile_User_IdAndTodoStatusOrderByCompletedAtDesc(Long userId, TodoStatus todoStatus);

    // 마이페이지 - 진행중인 할 일 목록 (category 같이 조회)
    @EntityGraph(attributePaths = "category")
    List<Todo> findAllByTerm_UserProfile_User_IdAndTodoStatusOrderByCreatedAtDesc(Long userId, TodoStatus todoStatus);

    // 마이페이지 - 같은 직무 유저들의 카테고리별 완료 개수 합계
    @Query("""
        SELECT td.category.id AS categoryId, COUNT(td) AS cnt
        FROM Todo td
        WHERE td.term.userProfile.job.id = :jobId
          AND td.todoStatus = :completedStatus
        GROUP BY td.category.id
        """)
    List<CategoryCompletedCountProjection> findCompletedCountsByJobIdGroupByCategory(
            @Param("jobId") Long jobId,
            @Param("completedStatus") TodoStatus completedStatus
    );

    interface CategoryCompletedCountProjection {
        Long getCategoryId();
        Long getCnt();
    }
}
