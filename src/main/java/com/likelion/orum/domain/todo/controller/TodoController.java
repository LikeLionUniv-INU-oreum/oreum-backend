package com.likelion.orum.domain.todo.controller;

import com.likelion.orum.domain.todo.dto.request.TodoCreateRequestDto;
import com.likelion.orum.domain.todo.dto.response.TodoCreateResponseDto;
import com.likelion.orum.domain.todo.service.TodoService;
import com.likelion.orum.global.exception.GeneralException;
import com.likelion.orum.global.exception.code.SecurityErrorCode;
import com.likelion.orum.global.response.ApiResponse;
import com.likelion.orum.global.security.principal.AuthenticatedUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TodoController {

    private final TodoService todoService;

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
}
