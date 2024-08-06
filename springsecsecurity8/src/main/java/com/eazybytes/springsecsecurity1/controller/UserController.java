package com.eazybytes.springsecsecurity1.controller;

import com.eazybytes.springsecsecurity1.dto.CustomerDto;
import com.eazybytes.springsecsecurity1.model.Customer;
import com.eazybytes.springsecsecurity1.service.CustomerUserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final CustomerUserService userService;

    @PostMapping("/register")
    public ResponseEntity<Long> registerUser(
            @RequestBody CustomerDto customerDto
    ) throws BadRequestException {
        System.out.println(customerDto);
        Long customer = userService.createCustomer(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @RequestMapping("/user")
    public Customer getUserDetailsAfterLogin(Authentication authentication){
        return userService.userLogin(authentication);
    }

}
