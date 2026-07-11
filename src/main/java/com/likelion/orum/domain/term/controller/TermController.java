package com.likelion.orum.domain.term.controller;

import com.likelion.orum.domain.term.dto.request.TermDashboardRequestDto;
import com.likelion.orum.domain.term.dto.response.TermDashboardResponseDto;
import com.likelion.orum.domain.term.dto.response.TermListResponseDto;
import com.likelion.orum.domain.term.service.TermService;
import com.likelion.orum.global.exception.GeneralException;
import com.likelion.orum.global.exception.code.SecurityErrorCode;
import com.likelion.orum.global.response.ApiResponse;
import com.likelion.orum.global.security.principal.AuthenticatedUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/terms")
@RequiredArgsConstructor
public class TermController {

    private final TermService termService;

    @GetMapping
    public ResponseEntity<ApiResponse<TermListResponseDto>> getTerms(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        Long userId = requireUserId(authenticatedUser);

        TermListResponseDto response = termService.getTerms(userId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<TermDashboardResponseDto>> getDashboard(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @ModelAttribute TermDashboardRequestDto request
    ) {
        if (authenticatedUser == null) {
            throw new GeneralException(SecurityErrorCode.AUTHENTICATION_REQUIRED);
        }

        TermDashboardResponseDto response = termService.getDashboard(authenticatedUser.userId(), request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    private Long requireUserId(AuthenticatedUser authenticatedUser) {
        if (authenticatedUser == null) {
            throw new GeneralException(SecurityErrorCode.AUTHENTICATION_REQUIRED);
        }

        return authenticatedUser.userId();
    }
}
