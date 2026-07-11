package com.likelion.orum.domain.user.controller;

import com.likelion.orum.domain.user.dto.request.OnboardingRequestDto;
import com.likelion.orum.domain.user.dto.response.OnboardingResponseDto;
import com.likelion.orum.domain.user.dto.response.UserInfoResponseDto;
import com.likelion.orum.domain.user.service.UserService;
import com.likelion.orum.global.exception.GeneralException;
import com.likelion.orum.global.exception.code.SecurityErrorCode;
import com.likelion.orum.global.response.ApiResponse;
import com.likelion.orum.global.security.principal.AuthenticatedUser;
import com.likelion.orum.domain.user.dto.request.UpdatePasswordRequestDto;
import com.likelion.orum.domain.user.dto.request.UpdateAcademicStatusRequestDto;
import com.likelion.orum.domain.user.dto.response.UpdateAcademicStatusResponseDto;
import com.likelion.orum.domain.user.dto.request.UpdateJobRequestDto;
import com.likelion.orum.domain.user.dto.response.UpdateJobResponseDto;
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

    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody UpdatePasswordRequestDto request
    ) {
        userService.updatePassword(authenticatedUser, request);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PatchMapping("/profile/academic-status")
    public ResponseEntity<ApiResponse<UpdateAcademicStatusResponseDto>> updateAcademicStatus(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody UpdateAcademicStatusRequestDto request
    ) {
        UpdateAcademicStatusResponseDto response = userService.updateAcademicStatus(authenticatedUser, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/profile/job")
    public ResponseEntity<ApiResponse<UpdateJobResponseDto>> updateJob(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody UpdateJobRequestDto request
    ) {
        UpdateJobResponseDto response = userService.updateJob(authenticatedUser, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<UserInfoResponseDto>> getMyInfo(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        Long userId = requireUserId(authenticatedUser);

        UserInfoResponseDto response = userService.getMyInfo(userId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    private Long requireUserId(AuthenticatedUser authenticatedUser) {
        if (authenticatedUser == null) {
            throw new GeneralException(SecurityErrorCode.AUTHENTICATION_REQUIRED);
        }

        return authenticatedUser.userId();
    }
}
