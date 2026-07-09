package com.likelion.orum.domain.home.service;

import com.likelion.orum.domain.home.dto.response.HomeResponseDto;
import com.likelion.orum.domain.home.exception.HomeErrorCode;
import com.likelion.orum.domain.todo.enums.TodoStatus;
import com.likelion.orum.domain.todo.repository.TodoRepository;
import com.likelion.orum.domain.user.entity.UserProfile;
import com.likelion.orum.domain.user.repository.UserProfileRepository;
import com.likelion.orum.global.exception.GeneralException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HomeService {

    private static final int RECENT_COMPLETED_COURSE_LIMIT = 3;

    private final UserProfileRepository userProfileRepository;
    private final TodoRepository todoRepository;

    @Transactional(readOnly = true)
    public HomeResponseDto getHome(Long userId) {
        UserProfile userProfile = userProfileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new GeneralException(HomeErrorCode.USER_PROFILE_NOT_FOUND));

        int currentHeight = todoRepository.findTotalHeightByUserId(userId, TodoStatus.COMPLETED).intValue();  // 직무 내 현재 고도(m)

        Long jobId = userProfile.getJob().getId();

        List<Long> jobHeights = todoRepository.findTotalHeightsByJobId(jobId, TodoStatus.COMPLETED);

        int jobTopPercent = calculateJobTopPercent(currentHeight, jobHeights); // 직무 내 상위 위치(%)

        // 직무별 최근 이수한 할 일 조회
        List<String> recentCompletedCourses = todoRepository.findRecentCompletedCourseNamesByJobId(
                jobId,
                userId,
                TodoStatus.COMPLETED,
                PageRequest.of(0, RECENT_COMPLETED_COURSE_LIMIT)
        );

        return HomeResponseDto.of(
                userProfile,
                currentHeight,
                jobTopPercent,
                recentCompletedCourses
        );
    }

    private int calculateJobTopPercent(int currentHeight, List<Long> jobHeights) {
        if (jobHeights.isEmpty()) return 100;

        long higherCount = jobHeights.stream()
                .filter(height -> height > currentHeight)
                .count();

        int rank = (int) higherCount + 1; // 나보다 점수 높은 사람 + 1 = 본인

        return (int) Math.ceil(rank * 100.0 / jobHeights.size());
    }
}