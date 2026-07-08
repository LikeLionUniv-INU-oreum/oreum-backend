package com.likelion.orum.domain.category.repository;

import com.likelion.orum.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
