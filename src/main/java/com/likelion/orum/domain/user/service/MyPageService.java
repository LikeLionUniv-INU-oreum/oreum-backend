package com.likelion.orum.domain.user.service;

import com.likelion.orum.domain.category.entity.Category;
import com.likelion.orum.domain.category.repository.CategoryRepository;
import com.likelion.orum.domain.todo.entity.Todo;
import com.likelion.orum.domain.todo.enums.TodoStatus;
import com.likelion.orum.domain.todo.repository.TodoRepository;
import com.likelion.orum.domain.term.repository.TermRepository;
import com.likelion.orum.domain.user.dto.response.CategoryStatResponseDto;
import com.likelion.orum.domain.user.dto.response.JobProgressResponseDto;
import com.likelion.orum.domain.user.dto.response.MountainSummaryResponseDto;
import com.likelion.orum.domain.user.dto.response.MyPageResponseDto;
import com.likelion.orum.domain.user.dto.response.MyPageUserResponseDto;
import com.likelion.orum.domain.user.dto.response.WaitingCourseResponseDto;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final TodoRepository todoRepository;
    private final TermRepository termRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public MyPageResponseDto getMyPage(AuthenticatedUser authenticatedUser) {
        Long userId = authenticatedUser.userId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(UserErrorCode.USER_NOT_FOUND));

        UserProfile userProfile = userProfileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new GeneralException(UserErrorCode.USER_PROFILE_NOT_FOUND));

        Long jobId = userProfile.getJob().getId();
        String jobName = userProfile.getJob().getJobName();

        return new MyPageResponseDto(
                MyPageUserResponseDto.of(user, userProfile),
                buildMountainSummary(userId),
                buildJobProgress(userId, jobId, jobName),
                buildCategoryStats(userId, jobId),
                buildWaitingCourses(userId)
        );
    }

    private MountainSummaryResponseDto buildMountainSummary(Long userId) {
        int currentAltitude = todoRepository.findTotalHeightByUserId(userId, TodoStatus.COMPLETED).intValue();
        long collectedFlagCount = todoRepository.countAllByTerm_UserProfile_User_IdAndTodoStatus(userId, TodoStatus.COMPLETED);
        long completedMountainCount = termRepository.countCompletedTermsByUserId(userId, TodoStatus.IN_PROGRESS);

        return new MountainSummaryResponseDto(currentAltitude, collectedFlagCount, completedMountainCount);
    }

    private JobProgressResponseDto buildJobProgress(Long userId, Long jobId, String jobName) {
        int currentAltitude = todoRepository.findTotalHeightByUserId(userId, TodoStatus.COMPLETED).intValue();
        List<Long> jobHeights = todoRepository.findTotalHeightsByJobId(jobId, TodoStatus.COMPLETED);

        int jobRankPercent = calculateJobTopPercent(currentAltitude, jobHeights);

        return new JobProgressResponseDto(jobName, jobRankPercent, buildJobProgressMessage(jobRankPercent));
    }

    private List<CategoryStatResponseDto> buildCategoryStats(Long userId, Long jobId) {
        List<Todo> completedTodos = todoRepository.findAllByTerm_UserProfile_User_IdAndTodoStatusOrderByCompletedAtDesc(
                userId, TodoStatus.COMPLETED
        );

        Map<Long, Long> myCountByCategory = completedTodos.stream()
                .collect(Collectors.groupingBy(todo -> todo.getCategory().getId(), Collectors.counting()));

        Map<Long, List<String>> completedCoursesByCategory = completedTodos.stream()
                .collect(Collectors.groupingBy(
                        todo -> todo.getCategory().getId(),
                        Collectors.mapping(Todo::getCourseName, Collectors.toList())
                ));

        // 평균 계산 분모: 같은 직무를 가진 전체 유저 수 (0명이면 나누기 방지용으로 1 취급)
        long jobMemberCount = Math.max(userProfileRepository.countByJob_Id(jobId), 1);

        Map<Long, Long> jobCountByCategory = todoRepository
                .findCompletedCountsByJobIdGroupByCategory(jobId, TodoStatus.COMPLETED).stream()
                .collect(Collectors.toMap(
                        TodoRepository.CategoryCompletedCountProjection::getCategoryId,
                        TodoRepository.CategoryCompletedCountProjection::getCnt
                ));

        List<Category> categories = categoryRepository.findAllByOrderByIdAsc();

        return categories.stream()
                .map(category -> toCategoryStat(category, myCountByCategory, jobCountByCategory, completedCoursesByCategory, jobMemberCount))
                .toList();
    }

    private CategoryStatResponseDto toCategoryStat(
            Category category,
            Map<Long, Long> myCountByCategory,
            Map<Long, Long> jobCountByCategory,
            Map<Long, List<String>> completedCoursesByCategory,
            long jobMemberCount
    ) {
        long myCount = myCountByCategory.getOrDefault(category.getId(), 0L);
        long jobTotalCount = jobCountByCategory.getOrDefault(category.getId(), 0L);
        double jobAverageCount = Math.round((jobTotalCount * 10.0) / jobMemberCount) / 10.0;
        List<String> completedCourses = completedCoursesByCategory.getOrDefault(category.getId(), List.of());

        return new CategoryStatResponseDto(
                category.getId(),
                category.getCategoryName(),
                myCount,
                jobAverageCount,
                completedCourses
        );
    }

    private List<WaitingCourseResponseDto> buildWaitingCourses(Long userId) {
        return todoRepository.findAllByTerm_UserProfile_User_IdAndTodoStatusOrderByCreatedAtDesc(
                        userId, TodoStatus.IN_PROGRESS
                ).stream()
                .map(WaitingCourseResponseDto::from)
                .toList();
    }

    private int calculateJobTopPercent(int currentHeight, List<Long> jobHeights) {
        if (jobHeights.isEmpty()) {
            return 100;
        }

        long higherCount = jobHeights.stream()
                .filter(height -> height > currentHeight)
                .count();

        int rank = (int) higherCount + 1; // 나보다 점수 높은 사람 + 1 = 본인

        return (int) Math.ceil(rank * 100.0 / jobHeights.size());
    }

    // 직무 내 상위 퍼센트 구간에 따른 응원 메시지. 문구/구간 기준은 추후 기획 확정되면 조정.
    private String buildJobProgressMessage(int jobRankPercent) {
        if (jobRankPercent <= 10) {
            return "정상이 눈앞이에요! 최고의 등반가예요 🏔️";
        } else if (jobRankPercent <= 30) {
            return "꾸준한 등반으로 더 높이 올라가고 있어요!";
        } else if (jobRankPercent <= 60) {
            return "차근차근 올라가고 있어요, 계속 힘내봐요!";
        } else {
            return "이제 막 등반을 시작했어요, 첫 발걸음을 내딛어봐요!";
        }
    }
}
