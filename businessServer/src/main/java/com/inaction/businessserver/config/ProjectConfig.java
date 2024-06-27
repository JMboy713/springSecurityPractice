package com.inaction.businessserver.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ProjectConfig {
    @Bean
    public RestTemplate restTemplate(){ // rest 엔드포인트를 호출하는데 사용
        return new RestTemplate();
    }
}
