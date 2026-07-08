package com.likelion.orum.domain.todo.dto.response;

import com.likelion.orum.domain.term.enums.TermType;
import com.likelion.orum.domain.todo.entity.Todo;
import com.likelion.orum.domain.todo.enums.TodoStatus;

public record TodoDetailResponseDto(
        Long todoId,
        Integer year,
        TermType termType,
        Long categoryId,
        String categoryName,
        String courseName,
        String weeklyPlan,
        TodoStatus todoStatus
) {

    public static TodoDetailResponseDto from(Todo todo) {
        return new TodoDetailResponseDto(
                todo.getId(),
                todo.getTerm().getYear(),
                todo.getTerm().getTermType(),
                todo.getCategory().getId(),
                todo.getCategory().getCategoryName(),
                todo.getCourseName(),
                todo.getWeeklyPlan(),
                todo.getTodoStatus()
        );
    }
}