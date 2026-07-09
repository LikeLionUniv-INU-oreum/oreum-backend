package com.likelion.orum.domain.todo.entity;

import com.likelion.orum.domain.category.entity.Category;
import com.likelion.orum.domain.common.entity.BaseTimeEntity;
import com.likelion.orum.domain.term.entity.Term;
import com.likelion.orum.domain.todo.enums.TodoStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "todos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id", nullable = false)
    private Term term;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "course_name", nullable = false, length = 100)
    private String courseName;

    @Lob
    @Column(name = "weekly_plan", nullable = false)
    private String weeklyPlan;

    @Enumerated(EnumType.STRING)
    @Column(name = "todo_status", nullable = false, length = 30)
    private TodoStatus todoStatus;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    // 할 일 완료 처리
    public void complete() {
        if (this.todoStatus != TodoStatus.IN_PROGRESS) {
            throw new IllegalStateException("진행중인 할 일만 완료 처리할 수 있습니다.");
        }

        this.todoStatus = TodoStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void update(Category category, String courseName, String weeklyPlan) {
        if (this.todoStatus != TodoStatus.IN_PROGRESS) {
            throw new IllegalStateException("진행중인 할 일만 수정할 수 있습니다.");
        }

        this.category = category;
        this.courseName = courseName;
        this.weeklyPlan = weeklyPlan;
    }

    public static Todo create(
            Term term,
            Category category,
            String courseName,
            String weeklyPlan
    ) {
        Todo todo = new Todo();
        todo.term = term;
        todo.category = category;
        todo.courseName = courseName;
        todo.weeklyPlan = weeklyPlan;
        todo.todoStatus = TodoStatus.IN_PROGRESS;
        return todo;
    }
}
