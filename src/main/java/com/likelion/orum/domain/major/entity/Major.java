package com.likelion.orum.domain.major.entity;

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
        name = "majors",
        uniqueConstraints = {
                // 동일한 전공명은 중복으로 등록될 수 없다.
                @UniqueConstraint(name = "uk_majors_major_name", columnNames = "major_name")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "major_id")
    private Long id;

    @Column(name = "major_name", nullable = false, length = 100)
    private String majorName;

    @Column(name = "college_name", nullable = false, length = 100)
    private String collegeName;

    public static Major create(String majorName, String collegeName) {
        Major major = new Major();
        major.majorName = majorName;
        major.collegeName = collegeName;
        return major;
    }
}
