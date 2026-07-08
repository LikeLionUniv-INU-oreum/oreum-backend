package com.likelion.orum.domain.job.service;

import com.likelion.orum.domain.job.dto.response.JobSearchResponseDto;
import com.likelion.orum.domain.job.entity.Job;
import com.likelion.orum.domain.job.repository.JobRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    @Transactional(readOnly = true)
    public List<JobSearchResponseDto> search(String keyword) {
        List<Job> jobs = (keyword == null || keyword.isBlank())
                ? jobRepository.findAll()
                : jobRepository.findByJobNameContaining(keyword);

        return jobs.stream()
                .map(JobSearchResponseDto::of)
                .toList();
    }
}