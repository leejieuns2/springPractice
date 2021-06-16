package com.example.springpractice;
// 어플리케이션 클래스의 위치는 default package로 권장함.

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
// SpringBootApplication 어노테이션이 아래의 두 어노테이션을 포괄하는 개념
// @ComponentScan
// @EnableAutoConfiguration // 자동 설정 관련 어노테이션
@EnableConfigurationProperties(JieunProperties.class)
public class SpringPracticeApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringPracticeApplication.class);

//        app.addListeners(new SampleListener());
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

}
