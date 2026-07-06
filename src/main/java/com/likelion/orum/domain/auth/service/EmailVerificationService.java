package com.likelion.orum.domain.auth.service;

import com.likelion.orum.domain.auth.entity.EmailVerification;
import com.likelion.orum.domain.auth.enums.VerificationPurpose;
import com.likelion.orum.domain.auth.exception.AuthErrorCode;
import com.likelion.orum.domain.auth.repository.EmailVerificationRepository;
import com.likelion.orum.domain.user.repository.UserRepository;
import java.security.SecureRandom;
import java.time.LocalDateTime;

import com.likelion.orum.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private static final int CODE_BOUND = 10_000;
    private static final int CODE_EXPIRE_MINUTES = 3;
    private static final String UNIVERSITY_EMAIL_SUFFIX = "@inu.ac.kr";

    private final EmailVerificationRepository emailVerificationRepository;
    private final UserRepository userRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public void sendSignupVerificationCode(String universityEmail) {
        validateNotRegisteredEmail(universityEmail);

        expirePreviousVerification(universityEmail);

        String code = generateCode();
        String codeHash = passwordEncoder.encode(code);

        EmailVerification emailVerification = EmailVerification.create(
                universityEmail,
                codeHash,
                LocalDateTime.now().plusMinutes(CODE_EXPIRE_MINUTES),
                VerificationPurpose.SIGN_UP
        );

        emailVerificationRepository.save(emailVerification);
        emailSender.sendVerificationCode(universityEmail, code);
    }

    @Transactional(noRollbackFor = GeneralException.class)
    public void confirmSignupVerificationCode(String universityEmail, String verificationCode) {
        EmailVerification emailVerification = emailVerificationRepository
                .findTopByUniversityEmailAndVerificationPurposeOrderByIdDesc(
                        universityEmail,
                        VerificationPurpose.SIGN_UP
                )
                .orElseThrow(() -> new GeneralException(AuthErrorCode.EMAIL_VERIFICATION_NOT_FOUND));

        if (emailVerification.isExpired()) {
            emailVerification.expire();
            throw new GeneralException(AuthErrorCode.EMAIL_VERIFICATION_EXPIRED);
        }

        if (!passwordEncoder.matches(verificationCode, emailVerification.getVerificationCodeHash())) {
            throw new GeneralException(AuthErrorCode.EMAIL_VERIFICATION_CODE_MISMATCH);
        }

        emailVerification.verify();
    }

    private void validateNotRegisteredEmail(String universityEmail) {
        if (userRepository.existsByUniversityEmail(universityEmail)) {
            throw new GeneralException(AuthErrorCode.EMAIL_ALREADY_REGISTERED);
        }
    }

    private void expirePreviousVerification(String universityEmail) {
        emailVerificationRepository
                .findTopByUniversityEmailAndVerificationPurposeOrderByIdDesc(
                        universityEmail,
                        VerificationPurpose.SIGN_UP
                )
                .ifPresent(EmailVerification::expire);
    }

    private String generateCode() {
        int number = secureRandom.nextInt(CODE_BOUND);
        return String.format("%04d", number);
    }
}
