package com.likelion.orum.domain.user.entity;

import com.likelion.orum.domain.common.entity.BaseTimeEntity;
import com.likelion.orum.domain.user.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
        name = "user",
        uniqueConstraints = {
                // 하나의 대학 이메일로는 하나의 계정만 만들 수 있다.
                @UniqueConstraint(name = "uk_user_university_email", columnNames = "university_email")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "university_email", nullable = false, length = 100)
    private String universityEmail;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "nickname", nullable = false, length = 30)
    private String nickname;

    @Column(name = "onboarding_completed", nullable = false)
    private Boolean onboardingCompleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false, length = 30)
    private UserStatus userStatus;

    public static User create(
            String universityEmail,
            String passwordHash,
            String nickname
    ) {
        User user = new User();
        user.universityEmail = universityEmail;
        user.passwordHash = passwordHash;
        user.nickname = nickname;
        user.onboardingCompleted = false;
        user.userStatus = UserStatus.ACTIVE;
        return user;
    }

    public void completeOnboarding() {
        this.onboardingCompleted = true;
    }

    public void changePassword(String newPasswordHash) {
        this.passwordHash = newPasswordHash;
    }
    
}
