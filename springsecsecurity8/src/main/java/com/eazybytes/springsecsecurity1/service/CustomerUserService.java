package com.eazybytes.springsecsecurity1.service;

import com.eazybytes.springsecsecurity1.dto.CustomerDto;
import com.eazybytes.springsecsecurity1.model.Customer;
import com.eazybytes.springsecsecurity1.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerUserService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public Long createCustomer(CustomerDto customerDto) throws BadRequestException {
        if(customerRepository.existsByEmail(customerDto.getEmail())){
            throw new BadRequestException("User already exists with username " + customerDto.getEmail());
        }
        String hashPwd = passwordEncoder.encode(customerDto.getPwd());
        customerDto.setPwd(hashPwd);
        Customer customer = CustomerMapper.mapToCustomer(customerDto);
        try {
            // Save the customer entity to the repository
            Customer savedCustomers = customerRepository.save(customer);

            // Return the ID of the newly created customer
            return savedCustomers.getId();
        } catch (DataAccessException e) {
            // Handle specific database exceptions
            throw new BadRequestException("An error occurred while saving the customer: " + e.getMessage(), e);
        } catch (Exception e) {
            // Handle any other exceptions
            throw new BadRequestException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }


    public Customer userLogin(Authentication authentication) {
        return customerRepository.findByEmail(authentication.getName()).orElse(null);
    }
}
