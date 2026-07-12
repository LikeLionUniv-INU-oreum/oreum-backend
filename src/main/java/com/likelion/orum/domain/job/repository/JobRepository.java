package com.likelion.orum.domain.job.repository;

import com.likelion.orum.domain.job.entity.Job;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByJobNameContaining(String keyword);

    Optional<Job> findByJobName(String jobName);
}
