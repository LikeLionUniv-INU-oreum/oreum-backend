package com.likelion.orum.domain.user.repository;

import com.likelion.orum.domain.user.entity.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    boolean existsByUserId(Long userId);

    @EntityGraph(attributePaths = {"user", "job", "major"})
    Optional<UserProfile> findByUser_Id(Long userId);

    // 마이페이지 - 같은 직무를 가진 전체 유저 수 (평균 계산용 분모)
    long countByJob_Id(Long jobId);
}
