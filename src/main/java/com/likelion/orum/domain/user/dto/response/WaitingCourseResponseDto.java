package com.likelion.orum.domain.user.dto.response;

import com.likelion.orum.domain.todo.entity.Todo;

// 진행중인 할 일 목록
public record WaitingCourseResponseDto(
        Long todoId,
        String courseName,
        String categoryName
) {

    public static WaitingCourseResponseDto from(Todo todo) {
        return new WaitingCourseResponseDto(
                todo.getId(),
                todo.getCourseName(),
                todo.getCategory().getCategoryName()
        );
    }
}
