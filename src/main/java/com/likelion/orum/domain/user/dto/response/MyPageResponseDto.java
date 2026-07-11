package com.likelion.orum.domain.user.dto.response;

import java.util.List;

public record MyPageResponseDto(
        MyPageUserResponseDto user,
        MountainSummaryResponseDto mountainSummary,
        JobProgressResponseDto jobProgress,
        List<CategoryStatResponseDto> categoryStats,
        List<WaitingCourseResponseDto> waitingCourses
) {
}
