package com.likelion.orum.domain.auth.controller;

import com.likelion.orum.domain.auth.dto.request.EmailVerificationConfirmRequestDto;
import com.likelion.orum.domain.auth.dto.request.EmailVerificationSendRequestDto;
import com.likelion.orum.domain.auth.dto.request.LoginRequestDto;
import com.likelion.orum.domain.auth.dto.request.SignupRequestDto;
import com.likelion.orum.domain.auth.dto.response.LoginResponseDto;
import com.likelion.orum.domain.auth.service.AuthService;
import com.likelion.orum.domain.auth.service.EmailVerificationService;
import com.likelion.orum.global.response.ApiResponse;
import com.likelion.orum.global.security.principal.AuthenticatedUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final EmailVerificationService emailVerificationService;
    private final AuthService authService;

    @PostMapping("/email-verifications")
    public ResponseEntity<ApiResponse<Void>> sendEmailVerificationCode(@Valid @RequestBody EmailVerificationSendRequestDto request) {
        emailVerificationService.sendSignupVerificationCode(request.universityEmail());
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/email-verifications/confirm")
    public ResponseEntity<ApiResponse<Void>> confirmEmailVerificationCode(@Valid @RequestBody EmailVerificationConfirmRequestDto request) {
        emailVerificationService.confirmSignupVerificationCode(
                request.universityEmail(),
                request.verificationCode()
        );
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupRequestDto request) {
        authService.signup(request);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto request) {
        LoginResponseDto response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        authService.logout(authenticatedUser);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
