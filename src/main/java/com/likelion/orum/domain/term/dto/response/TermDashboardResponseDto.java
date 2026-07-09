package com.likelion.orum.domain.term.dto.response;

import com.likelion.orum.domain.term.enums.TermType;
import com.likelion.orum.domain.todo.entity.Todo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** 분기별 베이스캠프 대시보드 응답 */
public record TermDashboardResponseDto(
        Integer year,
        TermType termType,
        Integer currentHeight,
        Integer jobPositionPercent,
        List<CategoryTodoGroupResponseDto> categories
) {

    public static TermDashboardResponseDto of(
            Integer year,
            TermType termType,
            Integer currentHeight,
            Integer jobPositionPercent,
            List<Todo> todos
    ) {
        // 분기별 할 일 -> 해당하는 각 카테고리 id와 매핑
        Map<Long, List<Todo>> groupedTodos = todos.stream()
                .collect(Collectors.groupingBy(
                        todo -> todo.getCategory().getId(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        // 카테고리 그룹 리스트
        List<CategoryTodoGroupResponseDto> categories = groupedTodos.values().stream()
                .map(CategoryTodoGroupResponseDto::from)
                .toList();

        return new TermDashboardResponseDto(
                year,
                termType,
                currentHeight,
                jobPositionPercent,
                categories
        );
    }

    public static TermDashboardResponseDto empty(Integer year, TermType termType) {
        return new TermDashboardResponseDto(
                year,
                termType,
                0,
                0,
                List.of()
        );
    }
}