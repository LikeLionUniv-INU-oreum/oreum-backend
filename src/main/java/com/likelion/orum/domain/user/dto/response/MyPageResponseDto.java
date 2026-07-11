package com.likelion.orum.domain.user.dto.response;

import com.likelion.orum.domain.user.entity.User;
import com.likelion.orum.domain.user.entity.UserProfile;

public record MyPageResponseDto(
        Long userId,
        String universityEmail,
        String nickname,
        Boolean onboardingCompleted,
        Long majorId,
        String majorName,
        Long jobId,
        String jobName,
        String academicStatus
) {

    public static MyPageResponseDto of(User user, UserProfile userProfile) {
        return new MyPageResponseDto(
                user.getId(),
                user.getUniversityEmail(),
                user.getNickname(),
                user.getOnboardingCompleted(),
                userProfile.getMajor().getId(),
                userProfile.getMajor().getMajorName(),
                userProfile.getJob().getId(),
                userProfile.getJob().getJobName(),
                userProfile.getAcademicStatus().name()
        );
    }
}
