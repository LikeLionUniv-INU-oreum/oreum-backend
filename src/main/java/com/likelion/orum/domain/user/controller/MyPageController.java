package com.likelion.orum.domain.user.controller;

import com.likelion.orum.domain.user.dto.response.MyPageResponseDto;
import com.likelion.orum.domain.user.service.UserService;
import com.likelion.orum.global.response.ApiResponse;
import com.likelion.orum.global.security.principal.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<MyPageResponseDto>> getMyPage(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        MyPageResponseDto response = userService.getMyPage(authenticatedUser);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
