package com.base.sbc.config.restTemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfiguration {

    private final RequestLoggingInterceptor requestLoggingInterceptor;
//    @Bean
//    public RestTemplateCustomizer restTemplateCustomizer() {
//        return restTemplate -> {
//            restTemplate.getInterceptors().add(new RequestLoggingInterceptor());
//            // 可以添加其他拦截器或自定义RestTemplate的配置
//        };
//    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(requestLoggingInterceptor);
        return restTemplate;
    }
}
