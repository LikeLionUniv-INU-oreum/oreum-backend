package com.likelion.orum.domain.home.dto.response;

import com.likelion.orum.domain.user.entity.UserProfile;
import java.util.List;

public record HomeResponseDto(
        String nickname,
        String jobName,
        Integer currentHeight,
        Integer jobTopPercent,
        List<String> recentCompletedCourses
) {

    public static HomeResponseDto of(
            UserProfile userProfile,
            Integer currentHeight,
            Integer jobTopPercent,
            List<String> recentCompletedCourses
    ) {
        return new HomeResponseDto(
                userProfile.getUser().getNickname(),
                userProfile.getJob().getJobName(),
                currentHeight,
                jobTopPercent,
                recentCompletedCourses
        );
    }
}