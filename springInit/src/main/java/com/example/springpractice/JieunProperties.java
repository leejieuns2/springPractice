package com.example.springpractice;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jieun")
public class JieunProperties {
    private String name;

    private int age;

    private String fullName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
