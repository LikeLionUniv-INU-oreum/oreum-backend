package com.likelion.orum.domain.major.dto.response;

import com.likelion.orum.domain.major.entity.Major;

public record MajorSearchResponseDto(
        Long majorId,
        String majorName,
        String collegeName
) {

    public static MajorSearchResponseDto of(Major major) {
        return new MajorSearchResponseDto(major.getId(), major.getMajorName(), major.getCollegeName());
    }
}
