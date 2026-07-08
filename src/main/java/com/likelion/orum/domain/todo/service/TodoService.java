package com.likelion.orum.domain.todo.service;

import com.likelion.orum.domain.category.entity.Category;
import com.likelion.orum.domain.category.repository.CategoryRepository;
import com.likelion.orum.domain.term.entity.Term;
import com.likelion.orum.domain.term.repository.TermRepository;
import com.likelion.orum.domain.todo.dto.request.TodoCreateRequestDto;
import com.likelion.orum.domain.todo.dto.response.TodoCreateResponseDto;
import com.likelion.orum.domain.todo.dto.response.TodoDetailResponseDto;
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

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final TermRepository termRepository;
    private final CategoryRepository categoryRepository;
    private final UserProfileRepository userProfileRepository;

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

    // 진행중인 할 일 조회
    @Transactional(readOnly = true)
    public TodoDetailResponseDto getTodoDetail(Long userId, Long todoId) {
        Todo todo = todoRepository.findWithDetailById(todoId)
                .orElseThrow(() -> new GeneralException(TodoErrorCode.TODO_NOT_FOUND));

        validateTodoOwner(todo, userId);
        validateInProgress(todo);

        return TodoDetailResponseDto.from(todo);
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

    private void validateTodoOwner(Todo todo, Long userId) {
        Long ownerId = todo.getTerm()
                .getUserProfile()
                .getUser()
                .getId();

        if (!ownerId.equals(userId)) {
            throw new GeneralException(TodoErrorCode.TODO_ACCESS_DENIED);
        }
    }

    private void validateInProgress(Todo todo) {
        if (todo.getTodoStatus() != TodoStatus.IN_PROGRESS) {
            throw new GeneralException(TodoErrorCode.TODO_NOT_IN_PROGRESS);
        }
    }
}
