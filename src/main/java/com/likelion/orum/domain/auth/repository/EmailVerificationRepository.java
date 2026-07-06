package com.likelion.orum.domain.auth.repository;

import com.likelion.orum.domain.auth.entity.EmailVerification;
import com.likelion.orum.domain.auth.enums.VerificationPurpose;
import com.likelion.orum.domain.auth.enums.VerificationStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    Optional<EmailVerification> findTopByUniversityEmailAndVerificationPurposeOrderByIdDesc(
            String universityEmail,
            VerificationPurpose verificationPurpose
    );

    boolean existsByUniversityEmailAndVerificationStatusAndVerificationPurpose(
            String universityEmail,
            VerificationStatus verificationStatus,
            VerificationPurpose verificationPurpose
    );
}
