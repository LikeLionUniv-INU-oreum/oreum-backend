package com.likelion.orum.domain.term.service;

import com.likelion.orum.domain.term.dto.request.TermDashboardRequestDto;
import com.likelion.orum.domain.term.dto.response.TermDashboardResponseDto;
import com.likelion.orum.domain.term.entity.Term;
import com.likelion.orum.domain.term.repository.TermRepository;
import com.likelion.orum.domain.todo.entity.Todo;
import com.likelion.orum.domain.todo.enums.TodoStatus;
import com.likelion.orum.domain.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TermService {

    private final TermRepository termRepository;
    private final TodoRepository todoRepository;

    @Transactional(readOnly = true)
    public TermDashboardResponseDto getDashboard(Long userId, TermDashboardRequestDto request) {
        return termRepository.findByUserProfile_User_IdAndYearAndTermType(userId, request.year(), request.termType())
                .map(term -> createDashboardResponse(request, term))
                .orElseGet(() -> TermDashboardResponseDto.empty(request.year(), request.termType())); // 해당 분기 데이터 없을 시
    }

    private TermDashboardResponseDto createDashboardResponse(TermDashboardRequestDto request, Term term) {
        List<Todo> todos = todoRepository.findAllByTerm_IdOrderByCreatedAtAsc(term.getId());

        int currentHeight = calculateCurrentHeight(todos); // 직무 내 현재 고도(m)

        Long jobId = term.getUserProfile().getJob().getId();

        List<Long> jobHeights = todoRepository.findHeightsByJobAndTerm(
                jobId,
                request.year(),
                request.termType(),
                TodoStatus.COMPLETED
        );

        int jobTopPercent = calculateJobTopPercent(currentHeight, jobHeights); // 직무 내 상위 위치(%)

        return TermDashboardResponseDto.of(
                request.year(),
                request.termType(),
                currentHeight,
                jobTopPercent,
                todos
        );
    }

    private int calculateCurrentHeight(List<Todo> todos) {
        return todos.stream()
                .filter(todo -> todo.getTodoStatus() == TodoStatus.COMPLETED)
                .mapToInt(todo -> todo.getCategory().getScore())
                .sum();
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
