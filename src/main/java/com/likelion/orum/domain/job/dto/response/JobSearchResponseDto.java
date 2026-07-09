package com.likelion.orum.domain.job.dto.response;

import com.likelion.orum.domain.job.entity.Job;

public record JobSearchResponseDto(
        Long jobId,
        String jobName
) {

    public static JobSearchResponseDto of(Job job) {
        return new JobSearchResponseDto(job.getId(), job.getJobName());
    }
}
