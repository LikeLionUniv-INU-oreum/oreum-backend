package com.likelion.orum.domain.todo.service;

import com.likelion.orum.domain.category.entity.Category;
import com.likelion.orum.domain.category.repository.CategoryRepository;
import com.likelion.orum.domain.review.entity.CourseReview;
import com.likelion.orum.domain.review.entity.ReviewRecommendedGrade;
import com.likelion.orum.domain.review.enums.RecommendedGrade;
import com.likelion.orum.domain.review.repository.CourseReviewRepository;
import com.likelion.orum.domain.review.repository.ReviewRecommendedGradeRepository;
import com.likelion.orum.domain.starcard.entity.StarCard;
import com.likelion.orum.domain.starcard.repository.StarCardRepository;
import com.likelion.orum.domain.term.entity.Term;
import com.likelion.orum.domain.term.repository.TermRepository;
import com.likelion.orum.domain.todo.dto.request.CourseReviewCreateRequestDto;
import com.likelion.orum.domain.todo.dto.request.TodoCreateRequestDto;
import com.likelion.orum.domain.todo.dto.request.TodoUpdateRequestDto;
import com.likelion.orum.domain.todo.dto.response.CourseReviewCreateResponseDto;
import com.likelion.orum.domain.todo.dto.response.TodoCreateResponseDto;
import com.likelion.orum.domain.todo.dto.response.TodoDetailResponseDto;
import com.likelion.orum.domain.todo.dto.response.TodoUpdateResponseDto;
import com.likelion.orum.domain.todo.entity.Todo;
import com.likelion.orum.domain.todo.enums.TodoStatus;
import com.likelion.orum.domain.todo.exception.TodoErrorCode;
import com.likelion.orum.domain.todo.repository.TodoRepository;
import com.likelion.orum.domain.user.entity.UserProfile;
import com.likelion.orum.domain.user.repository.UserProfileRepository;
import com.likelion.orum.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final TermRepository termRepository;
    private final CategoryRepository categoryRepository;
    private final UserProfileRepository userProfileRepository;
    private final CourseReviewRepository courseReviewRepository;
    private final StarCardRepository starCardRepository;
    private final ReviewRecommendedGradeRepository reviewRecommendedGradeRepository;

    // 새 할 일 생성
    @Transactional
    public TodoCreateResponseDto createTodo(Long userId, TodoCreateRequestDto request) {
        UserProfile userProfile = userProfileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new GeneralException(TodoErrorCode.USER_PROFILE_NOT_FOUND));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new GeneralException(TodoErrorCode.CATEGORY_NOT_FOUND));

        Term term = findOrCreateTerm(userProfile, request);

        Todo todo = Todo.create(
                term,
                category,
                request.courseName(),
                request.weeklyPlan()
        );

        Todo savedTodo = todoRepository.save(todo);

        return TodoCreateResponseDto.from(savedTodo);
    }

    // 할 일 상세 조회
    @Transactional(readOnly = true)
    public TodoDetailResponseDto getTodoDetail(Long userId, Long todoId) {
        Todo todo = getOwnedTodo(userId, todoId);

        return TodoDetailResponseDto.from(todo);
    }

    // 진행중인 할 일 수정
    @Transactional
    public TodoUpdateResponseDto updateTodo(Long userId, Long todoId, TodoUpdateRequestDto request) {
        Todo todo = getOwnedTodo(userId, todoId);

        validateInProgress(todo);
        validateUpdateRequest(request);

        Category category = null;

        if (request.categoryId() != null) {
            category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new GeneralException(TodoErrorCode.CATEGORY_NOT_FOUND));
        }

        todo.update(category, request.courseName(), request.weeklyPlan());

        return TodoUpdateResponseDto.from(todo);
    }

    // 진행중인 할 일 삭제
    @Transactional
    public void deleteTodo(Long userId, Long todoId) {
        Todo todo = getOwnedTodo(userId, todoId);
        validateInProgress(todo);

        todoRepository.delete(todo);
    }

    // 할 일 리뷰 작성
    @Transactional
    public CourseReviewCreateResponseDto createCourseReview(Long userId, Long todoId, CourseReviewCreateRequestDto request) {
        Todo todo = getOwnedTodo(userId, todoId);

        validateInProgress(todo);
        validateReviewNotExists(todoId);
        validateRecommendedGrades(request.recommendedGrades());

        CourseReview courseReview = CourseReview.create(
                todo,
                request.rating(),
                request.ascentGrade(),
                request.ascentSemester(),
                request.duration(),
                request.tip()
        );

        CourseReview savedCourseReview = courseReviewRepository.save(courseReview);

        List<ReviewRecommendedGrade> recommendedGrades = request.recommendedGrades().stream()
                .map(recommendedGrade -> ReviewRecommendedGrade.create(savedCourseReview, recommendedGrade))
                .toList();

        reviewRecommendedGradeRepository.saveAll(recommendedGrades);

        boolean starCardCreated = false;

        if (request.starCard() != null) {
            StarCard starCard = StarCard.create(
                    savedCourseReview,
                    request.starCard().situation(),
                    request.starCard().task(),
                    request.starCard().action(),
                    request.starCard().result()
            );

            starCardRepository.save(starCard);
            starCardCreated = true;
        }

        todo.complete();

        return CourseReviewCreateResponseDto.of(
                todo,
                savedCourseReview,
                starCardCreated
        );
    }

    private Term findOrCreateTerm(UserProfile userProfile, TodoCreateRequestDto request) {
        return termRepository.findByUserProfileAndYearAndTermType(
                        userProfile,
                        request.year(),
                        request.termType()
                )
                .orElseGet(() -> termRepository.save(
                        Term.create(
                                userProfile,
                                request.year(),
                                request.termType()
                        )
                ));
    }

    // 사용자가 소유한 할 일 가져오기
    private Todo getOwnedTodo(Long userId, Long todoId) {
        return todoRepository.findWithDetailByIdAndUserId(todoId, userId)
                .orElseThrow(() -> new GeneralException(TodoErrorCode.TODO_NOT_FOUND));
    }

    private void validateInProgress(Todo todo) {
        if (todo.getTodoStatus() != TodoStatus.IN_PROGRESS) {
            throw new GeneralException(TodoErrorCode.TODO_NOT_IN_PROGRESS);
        }
    }

    private void validateUpdateRequest(TodoUpdateRequestDto request) {
        if (
                request.categoryId() == null
                        && request.courseName() == null
                        && request.weeklyPlan() == null
        ) {
            throw new GeneralException(TodoErrorCode.INVALID_TODO_UPDATE);
        }
    }

    private void validateReviewNotExists(Long todoId) {
        if (courseReviewRepository.existsByTodo_Id(todoId)) {
            throw new GeneralException(TodoErrorCode.TODO_REVIEW_ALREADY_EXISTS);
        }
    }

    private void validateRecommendedGrades(List<RecommendedGrade> recommendedGrades) {
        boolean hasDuplicate = recommendedGrades.size() != recommendedGrades.stream()
                .distinct()
                .count();

        if (hasDuplicate) {
            throw new GeneralException(TodoErrorCode.INVALID_RECOMMENDED_GRADE);
        }

        if (recommendedGrades.contains(RecommendedGrade.ALL) && recommendedGrades.size() > 1) {
            throw new GeneralException(TodoErrorCode.INVALID_RECOMMENDED_GRADE);
        }
    }
}
