package com.eazybytes.springsecsecurity1.dto;

import lombok.Data;

@Data
public class CustomerDto {
    private String name;

    private String email;

    private String mobileNumber;

    private String pwd;

    private String role;
}
