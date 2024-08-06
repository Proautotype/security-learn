package com.eazybytes.springsecsecurity1.config;

import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.stereotype.Service;

@Service
public class EazyPasswordChecker implements CompromisedPasswordChecker {
    /**
     * @param password
     * @return
     */
    @Override
    public CompromisedPasswordDecision check(String password) {
        return new CompromisedPasswordDecision(false);
    }
}
