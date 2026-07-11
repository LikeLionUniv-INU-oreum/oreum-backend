package com.likelion.orum.domain.user.entity;

import com.likelion.orum.domain.common.entity.BaseTimeEntity;
import com.likelion.orum.domain.job.entity.Job;
import com.likelion.orum.domain.major.entity.Major;
import com.likelion.orum.domain.user.enums.AcademicStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "user_profiles",
        uniqueConstraints = {
                // 한 명의 유저는 하나의 프로필만 가질 수 있다.
                @UniqueConstraint(name = "uk_user_profiles_user_id", columnNames = "user_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_profile_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id", nullable = false)
    private Major major;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Enumerated(EnumType.STRING)
    @Column(name = "academic_status", nullable = false, length = 30)
    private AcademicStatus academicStatus;

    public static UserProfile create(User user, Major major, Job job, AcademicStatus academicStatus) {
        UserProfile userProfile = new UserProfile();
        userProfile.user = user;
        userProfile.major = major;
        userProfile.job = job;
        userProfile.academicStatus = academicStatus;
        return userProfile;
    }

    public void changeAcademicStatus(AcademicStatus academicStatus) {
        this.academicStatus = academicStatus;
    }
}
