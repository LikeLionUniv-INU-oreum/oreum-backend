package com.likelion.orum.domain.todo.dto.response;

import com.likelion.orum.domain.todo.entity.Todo;
import com.likelion.orum.domain.todo.enums.TodoStatus;

public record TodoUpdateResponseDto(
        Long todoId,
        Long categoryId,
        String categoryName,
        String courseName,
        String weeklyPlan,
        TodoStatus todoStatus
) {

    public static TodoUpdateResponseDto from(Todo todo) {
        return new TodoUpdateResponseDto(
                todo.getId(),
                todo.getCategory().getId(),
                todo.getCategory().getCategoryName(),
                todo.getCourseName(),
                todo.getWeeklyPlan(),
                todo.getTodoStatus()
        );
    }
}