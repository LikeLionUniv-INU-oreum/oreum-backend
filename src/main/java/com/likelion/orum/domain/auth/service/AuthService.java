package com.likelion.orum.domain.auth.service;

import com.likelion.orum.domain.auth.dto.request.SignupRequestDto;
import com.likelion.orum.domain.auth.enums.VerificationPurpose;
import com.likelion.orum.domain.auth.enums.VerificationStatus;
import com.likelion.orum.domain.auth.repository.EmailVerificationRepository;
import com.likelion.orum.domain.user.entity.User;
import com.likelion.orum.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequestDto request) {
        validateNotRegisteredEmail(request.universityEmail());
        validateVerifiedEmail(request.universityEmail());

        String passwordHash = passwordEncoder.encode(request.password());

        User user = User.create(
                request.universityEmail(),
                passwordHash,
                request.nickname()
        );

        userRepository.save(user);
    }

    private void validateNotRegisteredEmail(String universityEmail) {
        if (userRepository.existsByUniversityEmail(universityEmail)) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
    }

    private void validateVerifiedEmail(String universityEmail) {
        boolean verified = emailVerificationRepository
                .existsByUniversityEmailAndVerificationStatusAndVerificationPurpose(
                        universityEmail,
                        VerificationStatus.VERIFIED,
                        VerificationPurpose.SIGN_UP
                );

        if (!verified) {
            throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
        }
    }
}
