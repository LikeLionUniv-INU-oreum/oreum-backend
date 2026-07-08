package com.likelion.orum.domain.major.service;

import com.likelion.orum.domain.major.dto.response.MajorSearchResponseDto;
import com.likelion.orum.domain.major.entity.Major;
import com.likelion.orum.domain.major.repository.MajorRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MajorService {

    private final MajorRepository majorRepository;

    @Transactional(readOnly = true)
    public List<MajorSearchResponseDto> search(String keyword) {
        List<Major> majors = (keyword == null || keyword.isBlank())
                ? majorRepository.findAll()
                : majorRepository.findByMajorNameContaining(keyword);

        return majors.stream()
                .map(MajorSearchResponseDto::of)
                .toList();
    }
}
