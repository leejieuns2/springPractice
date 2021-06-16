package com.example.springpractice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // WebConfig class에서 addViewController를 overriding한 method가 대신 관련 역할을 수행하고 있음.
    // 특정 뷰로 이동하는 단순 로직의 경우 WebConfig에서 뷰를 연결해주는 것도 방법임. 데이터 전달시 Controller에서 method로 정의 필요.
//    @GetMapping("/hello")
//    public String hello() {
//        return "hello";
//    }

    @GetMapping("/my")
    public String my() {
        return "my";
    }

}
