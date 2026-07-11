package com.likelion.orum.domain.user.dto.response;

import com.likelion.orum.domain.user.entity.User;
import com.likelion.orum.domain.user.entity.UserProfile;

public record MyPageUserResponseDto(
        Long userId,
        String nickname,
        String universityEmail,
        String majorName,
        String academicStatus,
        String jobName
) {

    public static MyPageUserResponseDto of(User user, UserProfile userProfile) {
        return new MyPageUserResponseDto(
                user.getId(),
                user.getNickname(),
                user.getUniversityEmail(),
                userProfile.getMajor().getMajorName(),
                userProfile.getAcademicStatus().name(),
                userProfile.getJob().getJobName()
        );
    }
}
