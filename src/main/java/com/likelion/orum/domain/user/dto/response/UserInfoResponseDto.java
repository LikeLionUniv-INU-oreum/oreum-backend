package com.likelion.orum.domain.user.dto.response;

import com.likelion.orum.domain.user.entity.UserProfile;

public record UserInfoResponseDto(
        String nickname,
        String universityName,
        String majorName,
        String grade,
        String universityEmail
) {

    private static final String UNIVERSITY_NAME = "인천대학교";

    public static UserInfoResponseDto from(UserProfile userProfile) {
        return new UserInfoResponseDto(
                userProfile.getUser().getNickname(),
                UNIVERSITY_NAME,
                userProfile.getMajor().getMajorName(),
                userProfile.getAcademicStatus().getDisplayName(),
                userProfile.getUser().getUniversityEmail()
        );
    }
}