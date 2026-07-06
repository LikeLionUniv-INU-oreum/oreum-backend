package com.likelion.orum.domain.auth.service;

import com.likelion.orum.domain.auth.entity.EmailVerification;
import com.likelion.orum.domain.auth.enums.VerificationPurpose;
import com.likelion.orum.domain.auth.repository.EmailVerificationRepository;
import com.likelion.orum.domain.user.repository.UserRepository;
import java.security.SecureRandom;
import java.time.LocalDateTime;
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
        validateUniversityEmail(universityEmail);
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

    @Transactional
    public void confirmSignupVerificationCode(String universityEmail, String verificationCode) {
        EmailVerification emailVerification = emailVerificationRepository
                .findTopByUniversityEmailAndVerificationPurposeOrderByIdDesc(
                        universityEmail,
                        VerificationPurpose.SIGN_UP
                )
                .orElseThrow(() -> new IllegalArgumentException("전송된 인증번호가 없습니다."));

        if (emailVerification.isExpired()) {
            emailVerification.expire();
            throw new IllegalArgumentException("인증번호가 만료되었습니다.");
        }

        if (!passwordEncoder.matches(verificationCode, emailVerification.getVerificationCodeHash())) {
            throw new IllegalArgumentException("인증번호가 올바르지 않습니다.");
        }

        emailVerification.verify();
    }

    private void validateUniversityEmail(String universityEmail) {
        if (universityEmail == null || !universityEmail.endsWith(UNIVERSITY_EMAIL_SUFFIX)) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
    }

    private void validateNotRegisteredEmail(String universityEmail) {
        if (userRepository.existsByUniversityEmail(universityEmail)) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
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
