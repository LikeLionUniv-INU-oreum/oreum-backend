package com.likelion.orum.global.init;

import com.likelion.orum.domain.job.entity.Job;
import com.likelion.orum.domain.job.repository.JobRepository;
import com.likelion.orum.domain.major.entity.Major;
import com.likelion.orum.domain.major.repository.MajorRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 애플리케이션 최초 실행 시 전공/직무 마스터 데이터를 채워주는 러너.
 * 이미 데이터가 있으면 아무 것도 하지 않으므로 재시작해도 중복 삽입되지 않는다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InitialDataRunner implements ApplicationRunner {

    private final MajorRepository majorRepository;
    private final JobRepository jobRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedMajors();
        seedJobs();
    }

    private void seedMajors() {
        if (majorRepository.count() > 0) {
            return;
        }

        List<Major> majors = List.of(
                Major.create("국어국문학과", "인문대학"),
                Major.create("영어영문학과", "인문대학"),
                Major.create("사학과", "인문대학"),
                Major.create("정치외교학과", "사회과학대학"),
                Major.create("행정학과", "사회과학대학"),
                Major.create("경제학과", "사회과학대학"),
                Major.create("신문방송학과", "사회과학대학"),
                Major.create("경영학부", "경영대학"),
                Major.create("동북아국제통상학부", "동북아국제통상학부"),
                Major.create("수학과", "자연과학대학"),
                Major.create("물리학과", "자연과학대학"),
                Major.create("화학과", "자연과학대학"),
                Major.create("생명공학부", "생명과학기술대학"),
                Major.create("기계공학과", "공과대학"),
                Major.create("신소재공학과", "공과대학"),
                Major.create("전기공학과", "공과대학"),
                Major.create("정보통신공학과", "정보기술대학"),
                Major.create("컴퓨터공학부", "정보기술대학"),
                Major.create("임베디드시스템공학과", "정보기술대학"),
                Major.create("도시공학과", "도시과학대학")
        );

        majorRepository.saveAll(majors);
        log.info("Major 초기 데이터 {}건 저장 완료", majors.size());
    }

    private void seedJobs() {
        if (jobRepository.count() > 0) {
            return;
        }

        List<Job> jobs = List.of(
                Job.create("백엔드 개발자"),
                Job.create("프론트엔드 개발자"),
                Job.create("데이터 분석가"),
                Job.create("서비스 기획자"),
                Job.create("UX/UI 디자이너"),
                Job.create("마케팅"),
                Job.create("해외영업"),
                Job.create("국내영업"),
                Job.create("인사(HR)"),
                Job.create("재무/회계"),
                Job.create("경영지원"),
                Job.create("품질관리(QA)"),
                Job.create("생산관리"),
                Job.create("연구개발(R&D)"),
                Job.create("공무원")
        );

        jobRepository.saveAll(jobs);
        log.info("Job 초기 데이터 {}건 저장 완료", jobs.size());
    }
}
