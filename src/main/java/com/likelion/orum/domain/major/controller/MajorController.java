package com.likelion.orum.domain.major.controller;

import com.likelion.orum.domain.major.dto.response.MajorSearchResponseDto;
import com.likelion.orum.domain.major.service.MajorService;
import com.likelion.orum.global.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/majors")
public class MajorController {

    private final MajorService majorService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MajorSearchResponseDto>>> search(
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.success(majorService.search(keyword)));
    }
}

