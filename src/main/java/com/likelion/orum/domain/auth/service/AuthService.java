package com.likelion.orum.domain.auth.service;

import com.likelion.orum.domain.auth.dto.request.LoginRequestDto;
import com.likelion.orum.domain.auth.dto.request.SignupRequestDto;
import com.likelion.orum.domain.auth.dto.response.LoginResponseDto;
import com.likelion.orum.domain.auth.enums.VerificationPurpose;
import com.likelion.orum.domain.auth.enums.VerificationStatus;
import com.likelion.orum.domain.auth.exception.AuthErrorCode;
import com.likelion.orum.domain.auth.repository.EmailVerificationRepository;
import com.likelion.orum.domain.user.entity.User;
import com.likelion.orum.domain.user.enums.UserStatus;
import com.likelion.orum.domain.user.repository.UserRepository;
import com.likelion.orum.global.exception.GeneralException;
import com.likelion.orum.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

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

        try {
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            throw new GeneralException(AuthErrorCode.EMAIL_ALREADY_REGISTERED);
        }
    }

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByUniversityEmail(request.universityEmail())
                .orElseThrow(() -> new GeneralException(AuthErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new GeneralException(AuthErrorCode.LOGIN_FAILED);
        }

        if (user.getUserStatus() != UserStatus.ACTIVE) {
            throw new GeneralException(AuthErrorCode.USER_NOT_ACTIVE);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user);

        return LoginResponseDto.of(accessToken, user);
    }

    private void validateNotRegisteredEmail(String universityEmail) {
        if (userRepository.existsByUniversityEmail(universityEmail)) {
            throw new GeneralException(AuthErrorCode.EMAIL_ALREADY_REGISTERED);
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
            throw new GeneralException(AuthErrorCode.EMAIL_NOT_VERIFIED);
        }
    }
}
