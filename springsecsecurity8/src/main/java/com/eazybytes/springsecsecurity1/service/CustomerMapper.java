package com.eazybytes.springsecsecurity1.service;

import com.eazybytes.springsecsecurity1.dto.CustomerDto;
import com.eazybytes.springsecsecurity1.model.Customer;

public class CustomerMapper{
    public static CustomerDto mapToCustomerDto(Customer customers){
        CustomerDto customerDto = new CustomerDto();
        customerDto.setEmail(customers.getEmail());
        customerDto.setRole(customers.getRole());
        customerDto.setPwd(customers.getPwd());
        return customerDto;
    }

    public static Customer mapToCustomer(CustomerDto customerDto){
        Customer customers = new Customer();
        customers.setEmail(customerDto.getEmail());
        customers.setRole(customerDto.getRole());
        customers.setPwd(customerDto.getPwd());
        customers.setMobileNumber(customerDto.getMobileNumber());
        customers.setName(customerDto.getName());
        return customers;
    }

}