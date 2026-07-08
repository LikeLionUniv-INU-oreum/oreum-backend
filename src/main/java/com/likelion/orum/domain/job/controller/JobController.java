package com.likelion.orum.domain.job.controller;

import com.likelion.orum.domain.job.dto.response.JobSearchResponseDto;
import com.likelion.orum.domain.job.service.JobService;
import com.likelion.orum.global.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobSearchResponseDto>>> search(
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.success(jobService.search(keyword)));
    }
}
