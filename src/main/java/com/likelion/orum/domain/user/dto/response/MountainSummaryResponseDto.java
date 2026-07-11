package com.likelion.orum.domain.user.dto.response;

// 등반 요약 정보 (마이페이지 상단)
public record MountainSummaryResponseDto(
        Integer currentAltitude,       // 현재 고도(누적 완료 점수)
        Long collectedFlagCount,       // 누적 완료한 할 일(코스) 개수
        Long completedMountainCount    // 완등한 분기(산) 개수
) {
}
