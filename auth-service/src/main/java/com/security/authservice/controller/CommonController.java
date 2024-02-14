package com.security.authservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController {

    @GetMapping("/")
    public String healthCheck(){
        return "Service is Healthy";
    }

    @PostMapping("/test")
    public String test(@RequestBody String test){
        return test;
    }

}
