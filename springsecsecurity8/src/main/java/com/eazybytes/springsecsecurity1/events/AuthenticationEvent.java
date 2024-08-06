package com.eazybytes.springsecsecurity1.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationEvent {

    @EventListener
    public void onSuccessful(AuthenticationSuccessEvent authentication){
        log.info("Authentication successful with user {}", authentication.getAuthentication().getName());
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent authenticationFailureEvent){
        log.error("Authentication failed with {}",
                authenticationFailureEvent.getException().getMessage());
    }

}
