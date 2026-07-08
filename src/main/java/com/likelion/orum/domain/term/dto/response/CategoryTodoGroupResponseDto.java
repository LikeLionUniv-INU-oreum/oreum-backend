package com.likelion.orum.domain.term.dto.response;

import com.likelion.orum.domain.todo.entity.Todo;
import java.util.List;

/** 선택 카테고리 - 할 일 그룹화 */
public record CategoryTodoGroupResponseDto(
        Long categoryId,
        String categoryName,
        List<TodoSummaryResponseDto> todos
) {

    public static CategoryTodoGroupResponseDto from(List<Todo> todos) {
        Todo firstTodo = todos.get(0);

        return new CategoryTodoGroupResponseDto(
                firstTodo.getCategory().getId(),
                firstTodo.getCategory().getCategoryName(),
                todos.stream()
                        .map(TodoSummaryResponseDto::from)
                        .toList()
        );
    }
}