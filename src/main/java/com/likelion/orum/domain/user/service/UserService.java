package com.likelion.orum.domain.user.service;

import com.likelion.orum.domain.job.entity.Job;
import com.likelion.orum.domain.job.exception.JobErrorCode;
import com.likelion.orum.domain.job.repository.JobRepository;
import com.likelion.orum.domain.major.entity.Major;
import com.likelion.orum.domain.major.exception.MajorErrorCode;
import com.likelion.orum.domain.major.repository.MajorRepository;
import com.likelion.orum.domain.user.dto.request.OnboardingRequestDto;
import com.likelion.orum.domain.user.dto.response.*;
import com.likelion.orum.domain.user.entity.User;
import com.likelion.orum.domain.user.entity.UserProfile;
import com.likelion.orum.domain.user.enums.AcademicStatus;
import com.likelion.orum.domain.user.exception.UserErrorCode;
import com.likelion.orum.domain.user.repository.UserProfileRepository;
import com.likelion.orum.domain.user.repository.UserRepository;
import com.likelion.orum.global.exception.GeneralException;
import com.likelion.orum.global.security.principal.AuthenticatedUser;
import com.likelion.orum.domain.user.dto.request.UpdatePasswordRequestDto;
import com.likelion.orum.domain.user.dto.request.UpdateAcademicStatusRequestDto;
import com.likelion.orum.domain.user.dto.request.UpdateJobRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final MajorRepository majorRepository;
    private final JobRepository jobRepository;
    private final PasswordEncoder passwordEncoder;

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
    

    @Transactional
    public void updatePassword(AuthenticatedUser authenticatedUser, UpdatePasswordRequestDto request) {
        User user = userRepository.findById(authenticatedUser.userId())
                .orElseThrow(() -> new GeneralException(UserErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new GeneralException(UserErrorCode.CURRENT_PASSWORD_MISMATCH);
        }

        user.changePassword(passwordEncoder.encode(request.newPassword()));
    }

    @Transactional
    public UpdateAcademicStatusResponseDto updateAcademicStatus(
            AuthenticatedUser authenticatedUser,
            UpdateAcademicStatusRequestDto request
    ) {
        UserProfile userProfile = userProfileRepository.findByUser_Id(authenticatedUser.userId())
                .orElseThrow(() -> new GeneralException(UserErrorCode.USER_PROFILE_NOT_FOUND));

        AcademicStatus academicStatus = AcademicStatus.from(request.grade());

        userProfile.changeAcademicStatus(academicStatus);

        return new UpdateAcademicStatusResponseDto(userProfile.getAcademicStatus().getDisplayName());
    }

    @Transactional(readOnly = true)
    public UserInfoResponseDto getMyInfo(Long userId) {
        UserProfile userProfile = userProfileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new GeneralException(UserErrorCode.USER_NOT_FOUND));

        return UserInfoResponseDto.from(userProfile);
    }

    // 이미 온보딩을 완료한 유저는 재요청 불가
    private void validateNotOnboarded(User user) {
        boolean alreadyOnboarded = Boolean.TRUE.equals(user.getOnboardingCompleted())
                || userProfileRepository.existsByUserId(user.getId());

        if (alreadyOnboarded) {
            throw new GeneralException(UserErrorCode.ONBOARDING_ALREADY_COMPLETED);
        }
    }

    @Transactional
    public UpdateJobResponseDto updateJob(AuthenticatedUser authenticatedUser, UpdateJobRequestDto request) {
        UserProfile userProfile = userProfileRepository.findByUser_Id(authenticatedUser.userId())
                .orElseThrow(() -> new GeneralException(UserErrorCode.USER_PROFILE_NOT_FOUND));

        Job job = jobRepository.findById(request.jobId())
                .orElseThrow(() -> new GeneralException(JobErrorCode.JOB_NOT_FOUND));

        userProfile.changeJob(job);

        return new UpdateJobResponseDto(job.getId(), job.getJobName());
    }
}
