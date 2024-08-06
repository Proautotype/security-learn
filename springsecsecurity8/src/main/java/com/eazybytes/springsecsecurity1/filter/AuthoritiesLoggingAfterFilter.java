package com.eazybytes.springsecsecurity1.filter;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@Slf4j
public class AuthoritiesLoggingAfterFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(null != authentication){

            String message = String.format("User %s, is authenticated and has authorities %s",
                    authentication.getName(), authentication.getAuthorities().toString());

            log.info(message);

        }

        filterChain.doFilter(request, response);
    }
}
