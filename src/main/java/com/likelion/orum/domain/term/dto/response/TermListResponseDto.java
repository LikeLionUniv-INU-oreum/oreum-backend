package com.likelion.orum.domain.term.dto.response;

import com.likelion.orum.domain.term.entity.Term;
import com.likelion.orum.domain.term.enums.TermType;

import java.util.List;

public record TermListResponseDto(
        List<TermResponseDto> terms
) {

    public static TermListResponseDto from(List<Term> terms) {
        return new TermListResponseDto(
                terms.stream()
                        .map(TermResponseDto::from)
                        .toList()
        );
    }

    public record TermResponseDto(
            Integer year,
            TermType termType,
            String displayName
    ) {

        public static TermResponseDto from(Term term) {
            return new TermResponseDto(
                    term.getYear(),
                    term.getTermType(),
                    createDisplayName(term.getYear(), term.getTermType())
            );
        }

        private static String createDisplayName(Integer year, TermType termType) {
            return year + "년 " + convertTermTypeName(termType);
        }

        private static String convertTermTypeName(TermType termType) {
            return switch (termType) {
                case FIRST_HALF -> "상반기";
                case SECOND_HALF -> "하반기";
            };
        }
    }
}