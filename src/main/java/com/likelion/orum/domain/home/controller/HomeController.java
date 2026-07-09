package com.likelion.orum.domain.home.controller;

import com.likelion.orum.domain.home.dto.response.HomeResponseDto;
import com.likelion.orum.domain.home.service.HomeService;
import com.likelion.orum.global.exception.GeneralException;
import com.likelion.orum.global.exception.code.SecurityErrorCode;
import com.likelion.orum.global.response.ApiResponse;
import com.likelion.orum.global.security.principal.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/api/home")
    public ResponseEntity<ApiResponse<HomeResponseDto>> getHome(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        if (authenticatedUser == null) {
            throw new GeneralException(SecurityErrorCode.AUTHENTICATION_REQUIRED);
        }

        HomeResponseDto response = homeService.getHome(authenticatedUser.userId());

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}