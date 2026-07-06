package com.likelion.orum.domain.auth.controller;

import com.likelion.orum.domain.auth.dto.request.EmailVerificationConfirmRequestDto;
import com.likelion.orum.domain.auth.dto.request.EmailVerificationSendRequestDto;
import com.likelion.orum.domain.auth.dto.request.SignupRequestDto;
import com.likelion.orum.domain.auth.service.AuthService;
import com.likelion.orum.domain.auth.service.EmailVerificationService;
import com.likelion.orum.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final EmailVerificationService emailVerificationService;
    private final AuthService authService;

    @PostMapping("/email-verifications")
    public ResponseEntity<ApiResponse<Void>> sendEmailVerificationCode(@RequestBody EmailVerificationSendRequestDto request) {
        emailVerificationService.sendSignupVerificationCode(request.universityEmail());
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/email-verifications/confirm")
    public ResponseEntity<ApiResponse<Void>> confirmEmailVerificationCode(@RequestBody EmailVerificationConfirmRequestDto request) {
        emailVerificationService.confirmSignupVerificationCode(
                request.universityEmail(),
                request.verificationCode()
        );
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@RequestBody SignupRequestDto request) {
        authService.signup(request);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
