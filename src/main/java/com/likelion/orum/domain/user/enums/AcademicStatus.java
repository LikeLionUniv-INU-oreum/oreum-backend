package com.likelion.orum.domain.user.enums;

import com.likelion.orum.domain.user.exception.UserErrorCode;
import com.likelion.orum.global.exception.GeneralException;
import lombok.Getter;

@Getter
public enum AcademicStatus {
    FRESHMAN("1학년"),
    SOPHOMORE("2학년"),
    JUNIOR("3학년"),
    SENIOR("4학년"),
    EXTRA_SEMESTER("초과학기"),
    GRADUATE("졸업");

    private final String displayName;

    AcademicStatus(String displayName) {
        this.displayName = displayName;
    }

    public static AcademicStatus from(String value) {
        if (value == null || value.isBlank()) {
            throw new GeneralException(UserErrorCode.INVALID_ACADEMIC_STATUS);
        }

        String normalizedValue = value.trim();

        for (AcademicStatus academicStatus : values()) {
            if (    // 요청값 한글 or ENUM 둘 다 받아서 처리함
                    academicStatus.displayName.equals(normalizedValue)
                            || academicStatus.name().equalsIgnoreCase(normalizedValue)
            ) {
                return academicStatus;
            }
        }

        throw new GeneralException(UserErrorCode.INVALID_ACADEMIC_STATUS);
    }
}
