package com.likelion.orum.domain.user.controller;

import com.likelion.orum.domain.user.dto.request.OnboardingRequestDto;
import com.likelion.orum.domain.user.dto.response.OnboardingResponseDto;
import com.likelion.orum.domain.user.service.UserService;
import com.likelion.orum.global.response.ApiResponse;
import com.likelion.orum.global.security.principal.AuthenticatedUser;
import com.likelion.orum.domain.user.dto.request.UpdateAcademicStatusRequestDto;
import com.likelion.orum.domain.user.dto.response.UpdateAcademicStatusResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/me")
public class UserController {

    private final UserService userService;

    @PostMapping("/onboarding")
    public ResponseEntity<ApiResponse<OnboardingResponseDto>> onboarding(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody OnboardingRequestDto request
    ) {
        OnboardingResponseDto response = userService.completeOnboarding(authenticatedUser, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    @PatchMapping("/profile/academic-status")
    public ResponseEntity<ApiResponse<UpdateAcademicStatusResponseDto>> updateAcademicStatus(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody UpdateAcademicStatusRequestDto request
    ) {
        UpdateAcademicStatusResponseDto response = userService.updateAcademicStatus(authenticatedUser, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
