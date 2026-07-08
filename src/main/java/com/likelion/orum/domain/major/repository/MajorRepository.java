package com.likelion.orum.domain.major.repository;

import com.likelion.orum.domain.major.entity.Major;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MajorRepository extends JpaRepository<Major, Long> {

    List<Major> findByMajorNameContaining(String keyword);
}
