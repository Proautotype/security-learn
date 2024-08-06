package com.eazybytes.springsecsecurity1.config.authProviders;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class EazyByteAuthenticationProviderProd implements AuthenticationProvider {

    /*
    every auth provider needs two elements
    1. UserDetailService bean
    2. Password Encoder
     */
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public Authentication authenticate(Authentication authentication){
        String username = authentication.getName();
        String rawPwd = authentication.getCredentials().toString();
        //load the user details via user
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(passwordEncoder.matches(rawPwd, userDetails.getPassword())){

            //you could all other custom validation here, like checking age

            return new UsernamePasswordAuthenticationToken(username, rawPwd, userDetails.getAuthorities());
        }else{
            throw new BadCredentialsException("Invalid password!");
        }
    }

    /**
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
       return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }


}
