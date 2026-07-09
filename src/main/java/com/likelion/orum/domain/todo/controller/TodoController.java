package com.likelion.orum.domain.todo.controller;

import com.likelion.orum.domain.todo.dto.request.CourseReviewCreateRequestDto;
import com.likelion.orum.domain.todo.dto.request.TodoCreateRequestDto;
import com.likelion.orum.domain.todo.dto.request.TodoUpdateRequestDto;
import com.likelion.orum.domain.todo.dto.response.CourseReviewCreateResponseDto;
import com.likelion.orum.domain.todo.dto.response.TodoCreateResponseDto;
import com.likelion.orum.domain.todo.dto.response.TodoDetailResponseDto;
import com.likelion.orum.domain.todo.dto.response.TodoUpdateResponseDto;
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
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    // 새 할 일 생성
    @PostMapping
    public ResponseEntity<ApiResponse<TodoCreateResponseDto>> createTodo(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody TodoCreateRequestDto request
    ) {
        Long userId = requireUserId(authenticatedUser);

        TodoCreateResponseDto response = todoService.createTodo(userId, request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 진행중인 할 일 조회
    @GetMapping("/{todoId}")
    public ResponseEntity<ApiResponse<TodoDetailResponseDto>> getTodoDetail(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long todoId
    ) {
        Long userId = requireUserId(authenticatedUser);

        TodoDetailResponseDto response = todoService.getTodoDetail(userId, todoId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 진행중인 할 일 수정
    @PatchMapping("/{todoId}")
    public ResponseEntity<ApiResponse<TodoUpdateResponseDto>> updateTodo(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long todoId,
            @Valid @RequestBody TodoUpdateRequestDto request
    ) {
        Long userId = requireUserId(authenticatedUser);

        TodoUpdateResponseDto response = todoService.updateTodo(userId, todoId, request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 진행중인 할 일 삭제
    @DeleteMapping("/{todoId}")
    public ResponseEntity<ApiResponse<Void>> deleteTodo(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long todoId
    ) {
        Long userId = requireUserId(authenticatedUser);

        todoService.deleteTodo(userId, todoId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 할 일 리뷰 작성
    @PostMapping("/{todoId}/course-review")
    public ResponseEntity<ApiResponse<CourseReviewCreateResponseDto>> createCourseReview(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long todoId,
            @Valid @RequestBody CourseReviewCreateRequestDto request
    ) {
        Long userId = requireUserId(authenticatedUser);

        CourseReviewCreateResponseDto response = todoService.createCourseReview(userId, todoId, request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    private Long requireUserId(AuthenticatedUser authenticatedUser) {
        if (authenticatedUser == null) {
            throw new GeneralException(SecurityErrorCode.AUTHENTICATION_REQUIRED);
        }

        return authenticatedUser.userId();
    }
}
