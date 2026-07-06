package com.likelion.orum.domain.user.repository;

import com.likelion.orum.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUniversityEmail(String universityEmail);

    Optional<User> findByUniversityEmail(String universityEmail);
}
