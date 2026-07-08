package com.likelion.orum.domain.term.dto.response;

import com.likelion.orum.domain.todo.entity.Todo;
import com.likelion.orum.domain.todo.enums.TodoStatus;

/** 할 일 (제목, 상태) 그룹화 */
public record TodoSummaryResponseDto(
        Long todoId,
        String courseName,
        TodoStatus todoStatus
) {

    public static TodoSummaryResponseDto from(Todo todo) {
        return new TodoSummaryResponseDto(
                todo.getId(),
                todo.getCourseName(),
                todo.getTodoStatus()
        );
    }
}
