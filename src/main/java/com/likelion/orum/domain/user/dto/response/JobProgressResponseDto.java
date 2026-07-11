package com.likelion.orum.domain.user.dto.response;

// 직무 내 등반 순위
public record JobProgressResponseDto(
        String jobName,
        Integer jobRankPercent,
        String message
) {
}
