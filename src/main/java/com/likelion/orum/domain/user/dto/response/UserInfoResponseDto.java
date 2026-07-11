package com.likelion.orum.domain.user.dto.response;

import com.likelion.orum.domain.user.entity.UserProfile;
import com.likelion.orum.domain.user.enums.AcademicStatus;

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
                toGradeName(userProfile.getAcademicStatus()),
                userProfile.getUser().getUniversityEmail()
        );
    }

    private static String toGradeName(AcademicStatus academicStatus) {
        return switch (academicStatus) {
            case FRESHMAN -> "1학년";
            case SOPHOMORE -> "2학년";
            case JUNIOR -> "3학년";
            case SENIOR -> "4학년";
            case EXTRA_SEMESTER -> "초과학기";
            case GRADUATE -> "졸업";
        };
    }
}