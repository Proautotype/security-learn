package com.eazybytes.springsecsecurity1.config;

import com.eazybytes.springsecsecurity1.model.Customer;
import com.eazybytes.springsecsecurity1.repository.CustomerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EazyBankUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public EazyBankUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customers = customerRepository.findByEmail(username).orElseThrow(
                ()-> new UsernameNotFoundException( String.format("User details not found for the user {}", username) )
        );
        List<SimpleGrantedAuthority> authorities =
                customers.getAuthorities().stream().map(authority ->
                        new SimpleGrantedAuthority(authority.getName())).collect(Collectors.toList());

        return new User(customers.getEmail(), customers.getPwd(),authorities);
    }

}
