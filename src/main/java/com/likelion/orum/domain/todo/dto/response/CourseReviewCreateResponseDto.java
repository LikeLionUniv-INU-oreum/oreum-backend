package com.likelion.orum.domain.todo.dto.response;

import com.likelion.orum.domain.review.entity.CourseReview;
import com.likelion.orum.domain.todo.entity.Todo;
import com.likelion.orum.domain.todo.enums.TodoStatus;

public record CourseReviewCreateResponseDto(
        Long todoId,
        Long courseReviewId,
        TodoStatus todoStatus,
        boolean starCardCreated
) {

    public static CourseReviewCreateResponseDto of(
            Todo todo,
            CourseReview courseReview,
            boolean starCardCreated
    ) {
        return new CourseReviewCreateResponseDto(
                todo.getId(),
                courseReview.getId(),
                todo.getTodoStatus(),
                starCardCreated
        );
    }
}