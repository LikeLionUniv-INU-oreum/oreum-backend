package com.likelion.orum.domain.auth.entity;

import com.likelion.orum.domain.auth.enums.VerificationPurpose;
import com.likelion.orum.domain.auth.enums.VerificationStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "email_verification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_verification_id")
    private Long id;

    @Column(name = "university_email", nullable = false, length = 100)
    private String universityEmail;

    @Column(name = "verification_code_hash", nullable = false, length = 255)
    private String verificationCodeHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false, length = 30)
    private VerificationStatus verificationStatus;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_purpose", nullable = false, length = 30)
    private VerificationPurpose verificationPurpose;

    public static EmailVerification create(
            String universityEmail,
            String verificationCodeHash,
            LocalDateTime expiresAt,
            VerificationPurpose verificationPurpose
    ) {
        EmailVerification emailVerification = new EmailVerification();
        emailVerification.universityEmail = universityEmail;
        emailVerification.verificationCodeHash = verificationCodeHash;
        emailVerification.verificationStatus = VerificationStatus.PENDING;
        emailVerification.expiresAt = expiresAt;
        emailVerification.verificationPurpose = verificationPurpose;
        return emailVerification;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public void verify() {
        this.verificationStatus = VerificationStatus.VERIFIED;
    }

    public void expire() {
        this.verificationStatus = VerificationStatus.EXPIRED;
    }
}
