package com.likelion.orum.domain.todo.dto.response;

import com.likelion.orum.domain.todo.entity.Todo;
import com.likelion.orum.domain.todo.enums.TodoStatus;

public record TodoCreateResponseDto(
        Long todoId,
        Integer year,
        String termType,
        Long categoryId,
        String categoryName,
        String courseName,
        String weeklyPlan,
        TodoStatus todoStatus
) {

    public static TodoCreateResponseDto from(Todo todo) {
        return new TodoCreateResponseDto(
                todo.getId(),
                todo.getTerm().getYear(),
                todo.getTerm().getTermType().name(),
                todo.getCategory().getId(),
                todo.getCategory().getCategoryName(),
                todo.getCourseName(),
                todo.getWeeklyPlan(),
                todo.getTodoStatus()
        );
    }
}
