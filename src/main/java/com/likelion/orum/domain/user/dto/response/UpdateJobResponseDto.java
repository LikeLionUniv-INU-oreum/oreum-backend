package com.likelion.orum.domain.user.dto.response;

import com.likelion.orum.domain.job.entity.Job;

public record UpdateJobResponseDto(
        Long jobId,
        String jobName
) {

    public static UpdateJobResponseDto from(Job job) {
        return new UpdateJobResponseDto(
                job.getId(),
                job.getJobName()
        );
    }
}
