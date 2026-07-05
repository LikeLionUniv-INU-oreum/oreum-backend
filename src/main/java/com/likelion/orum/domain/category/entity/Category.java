package com.likelion.orum.domain.category.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "categories",
        uniqueConstraints = {
                // 동일한 카테고리명은 중복으로 등록될 수 없다.
                @UniqueConstraint(name = "uk_categories_category_name", columnNames = "category_name")
        }
)@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name", nullable = false, length = 50)
    private String categoryName;

    @Column(name = "score", nullable = false)
    private Integer score;
}
