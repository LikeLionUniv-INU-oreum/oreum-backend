package com.likelion.orum.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Security Filter에서 ApiResponse 객체를 JSON 응답으로 변환하기 위한 설정 클래스.
 *
 * Security Filter는 DispatcherServlet 이전에 동작하므로,
 * ObjectMapper를 사용해 HttpServletResponse에 직접 JSON을 작성한다.
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
