package com.example.springpractice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 추가적인 웹 설정 가능
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // implement 하는 WebMvcConfigurer의 메소드들을 재정의하여 설정함.


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/m/**")
                .addResourceLocations("classpath:/m/")
                .setCachePeriod(20);
    }
}
