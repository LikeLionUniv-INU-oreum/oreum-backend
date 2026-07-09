package com.likelion.orum.domain.user.repository;

import com.likelion.orum.domain.user.entity.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    boolean existsByUserId(Long userId);

    @EntityGraph(attributePaths = {"user", "job"})
    Optional<UserProfile> findByUser_Id(Long userId);
}
