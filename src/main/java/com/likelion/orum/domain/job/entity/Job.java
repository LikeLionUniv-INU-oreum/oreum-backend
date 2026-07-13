package com.likelion.orum.domain.job.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "jobs",
        uniqueConstraints = {
                // 동일한 직무명은 중복으로 등록될 수 없다.
                @UniqueConstraint(name = "uk_jobs_job_name", columnNames = "job_name")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long id;

    @Column(name = "job_name", nullable = false, length = 50)
    private String jobName;

    public static Job create(String jobName) {
        Job job = new Job();
        job.jobName = jobName;
        return job;
    }
}
