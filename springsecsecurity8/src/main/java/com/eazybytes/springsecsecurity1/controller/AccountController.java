package com.eazybytes.springsecsecurity1.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @GetMapping("/myAccounts")
    public String sayWelcome(){
        return "Welcome to Spring app with security";
    }

}
