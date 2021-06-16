package com.example.springpractice;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CORSSampleController {

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/cors")
    public String hello() {
        return "cors";
    }
}
