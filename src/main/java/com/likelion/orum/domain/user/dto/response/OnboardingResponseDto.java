package com.likelion.orum.domain.user.dto.response;

import com.likelion.orum.domain.user.entity.UserProfile;

public record OnboardingResponseDto(
        Long userId,
        Long userProfileId,
        Long majorId,
        String majorName,
        Long jobId,
        String jobName,
        String academicStatus,
        Boolean onboardingCompleted
) {

    public static OnboardingResponseDto of(UserProfile userProfile) {
        return new OnboardingResponseDto(
                userProfile.getUser().getId(),
                userProfile.getId(),
                userProfile.getMajor().getId(),
                userProfile.getMajor().getMajorName(),
                userProfile.getJob().getId(),
                userProfile.getJob().getJobName(),
                userProfile.getAcademicStatus().name(),
                userProfile.getUser().getOnboardingCompleted()
        );
    }
}
