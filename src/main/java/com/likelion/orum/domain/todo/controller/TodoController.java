package com.likelion.orum.domain.todo.controller;

import com.likelion.orum.domain.todo.dto.request.CourseReviewCreateRequestDto;
import com.likelion.orum.domain.todo.dto.request.TodoCreateRequestDto;
import com.likelion.orum.domain.todo.dto.response.CourseReviewCreateResponseDto;
import com.likelion.orum.domain.todo.dto.response.TodoCreateResponseDto;
import com.likelion.orum.domain.todo.dto.response.TodoDetailResponseDto;
import com.likelion.orum.domain.todo.service.TodoService;
import com.likelion.orum.global.exception.GeneralException;
import com.likelion.orum.global.exception.code.SecurityErrorCode;
import com.likelion.orum.global.response.ApiResponse;
import com.likelion.orum.global.security.principal.AuthenticatedUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class TodoController {

    private final TodoService todoService;

    // 새 할 일 생성
    @PostMapping("/api/todos")
    public ResponseEntity<ApiResponse<TodoCreateResponseDto>> createTodo(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody TodoCreateRequestDto request
    ) {
        if (authenticatedUser == null) {
            throw new GeneralException(SecurityErrorCode.AUTHENTICATION_REQUIRED);
        }

        TodoCreateResponseDto response = todoService.createTodo(authenticatedUser.userId(), request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 진행중인 할 일 조회
    @GetMapping("/api/todos/{todoId}")
    public ResponseEntity<ApiResponse<TodoDetailResponseDto>> getTodoDetail(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long todoId
    ) {
        if (authenticatedUser == null) {
            throw new GeneralException(SecurityErrorCode.AUTHENTICATION_REQUIRED);
        }

        TodoDetailResponseDto response = todoService.getTodoDetail(authenticatedUser.userId(), todoId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 할 일 리뷰 작성
    @PostMapping("/api/todos/{todoId}/course-review")
    public ResponseEntity<ApiResponse<CourseReviewCreateResponseDto>> createCourseReview(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long todoId,
            @Valid @RequestBody CourseReviewCreateRequestDto request
    ) {
        if (authenticatedUser == null) {
            throw new GeneralException(SecurityErrorCode.AUTHENTICATION_REQUIRED);
        }

        CourseReviewCreateResponseDto response = todoService.createCourseReview(authenticatedUser.userId(), todoId, request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
