package com.likelion.orum.domain.user.dto.response;

import java.util.List;

// 카테고리별 나의 완료 수 vs 같은 직무 유저 평균 완료 수 비교
public record CategoryStatResponseDto(
        Long categoryId,
        String categoryName,
        Long myCount,
        Double jobAverageCount,
        List<String> completedCourses
) {
}
