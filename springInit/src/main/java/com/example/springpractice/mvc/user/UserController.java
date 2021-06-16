package com.example.springpractice.mvc.user;

import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

//    @PostMapping("/user")
//    // 보통 JSON 컨버터 사용해서 Response와 Request를 변환함.
//    public @ResponseBody User create(@RequestBody User user) {
//        return null;
//    }

    @PostMapping("/users/create")
    public User create(@RequestBody User user) {
        return user;
    }
}
