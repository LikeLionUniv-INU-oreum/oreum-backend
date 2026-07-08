package com.likelion.orum.domain.user.service;

import com.likelion.orum.domain.job.entity.Job;
import com.likelion.orum.domain.job.exception.JobErrorCode;
import com.likelion.orum.domain.job.repository.JobRepository;
import com.likelion.orum.domain.major.entity.Major;
import com.likelion.orum.domain.major.exception.MajorErrorCode;
import com.likelion.orum.domain.major.repository.MajorRepository;
import com.likelion.orum.domain.user.dto.request.OnboardingRequestDto;
import com.likelion.orum.domain.user.dto.response.OnboardingResponseDto;
import com.likelion.orum.domain.user.entity.User;
import com.likelion.orum.domain.user.entity.UserProfile;
import com.likelion.orum.domain.user.exception.UserErrorCode;
import com.likelion.orum.domain.user.repository.UserProfileRepository;
import com.likelion.orum.domain.user.repository.UserRepository;
import com.likelion.orum.global.exception.GeneralException;
import com.likelion.orum.global.security.principal.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final MajorRepository majorRepository;
    private final JobRepository jobRepository;

    @Transactional
    public OnboardingResponseDto completeOnboarding(AuthenticatedUser authenticatedUser, OnboardingRequestDto request) {
        User user = userRepository.findById(authenticatedUser.userId())
                .orElseThrow(() -> new GeneralException(UserErrorCode.USER_NOT_FOUND));

        validateNotOnboarded(user);

        Major major = majorRepository.findById(request.majorId())
                .orElseThrow(() -> new GeneralException(MajorErrorCode.MAJOR_NOT_FOUND));

        Job job = jobRepository.findById(request.jobId())
                .orElseThrow(() -> new GeneralException(JobErrorCode.JOB_NOT_FOUND));

        UserProfile userProfile = UserProfile.create(user, major, job, request.academicStatus());
        userProfileRepository.save(userProfile);

        user.completeOnboarding();

        return OnboardingResponseDto.of(userProfile);
    }

    // 이미 온보딩을 완료한 유저는 재요청 불가
    private void validateNotOnboarded(User user) {
        boolean alreadyOnboarded = Boolean.TRUE.equals(user.getOnboardingCompleted())
                || userProfileRepository.existsByUserId(user.getId());

        if (alreadyOnboarded) {
            throw new GeneralException(UserErrorCode.ONBOARDING_ALREADY_COMPLETED);
        }
    }
}
