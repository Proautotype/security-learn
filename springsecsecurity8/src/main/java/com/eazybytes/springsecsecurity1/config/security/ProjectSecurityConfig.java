package com.eazybytes.springsecsecurity1.config.security;

import com.eazybytes.springsecsecurity1.filter.AuthoritiesLoggingAfterFilter;
import com.eazybytes.springsecsecurity1.filter.RequestValidationBeforeFilter;
import com.eazybytes.springsecsecurity1.config.CsrfCookieFilter;
import com.eazybytes.springsecsecurity1.exceptions.CustomAccessDeniedHandler;
import com.eazybytes.springsecsecurity1.exceptions.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Profile("!prod")
@Configuration
public class ProjectSecurityConfig {

    @Bean
    public SecurityFilterChain doFilter(HttpSecurity http) throws Exception {
        //read the csrf token from the request
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();

        http.securityContext(contextConfig -> contextConfig.requireExplicitSave(false));
        //the spring security to generate session always
        http.securityContext(contextConfig -> contextConfig.requireExplicitSave(false))
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

        http.cors(corsConfig -> corsConfig.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));//the origins that is allowed to reach our app
            config.setAllowedMethods(Collections.singletonList("*"));//state all the allowed methods
            config.setAllowCredentials(true);
            config.setAllowedHeaders(Collections.singletonList("*"));//state all the allowed headers
            config.setMaxAge(3600L);//max time to wait before requesting pass
            return config;
        }));
        /*
            This CSRF settings generate CSRF token (which is a cookie with key: X-XSRF-token) lazily,
            that is until side effect methods are invoked
            to generate this token for all methods and route gingerly kindly use a filter
            in this project I used CsrfCookieFilter.

            This generates the csrf cookie right after the login
        * */
        http.csrf(csrfConfig -> csrfConfig
                        //reads and compare csrf tokens in both the cookie and request
                        .csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                        //exempt these routes from csrf protection
                        .ignoringRequestMatchers("/contact", "/register")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                //put the filter to generate to csrf token after the basic authentication filter class
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                //put validation filter before basic authentication
                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class);

        //https configuration
        http.requiresChannel(registry -> registry.anyRequest().requiresInsecure()) //HTTP only
                //define all routes with their permissions (secured (authenticated) or open (permit) )
                .authorizeHttpRequests(requests -> requests
                                //accepting request based on authority (fine grain)
                                .requestMatchers("/myAccounts").hasAuthority("VIEWACCOUNT")
                                .requestMatchers("/myBalance").hasAnyAuthority("VIEWACCOUNT", "VIEWBALANCE")
                                .requestMatchers("/myCards").hasAuthority("VIEWCARDS")
                                .requestMatchers("/myLoans").hasAuthority("VIEWLOANS")
                                .requestMatchers("/user").authenticated()
                                //accepting request based on role (coarse grain)
//                        .requestMatchers("/accounts").hasAuthority("")
//                        .requestMatchers("/balance").hasAnyAuthority("")
//                        .requestMatchers("/cards").hasAuthority("")
//                        .requestMatchers("/loans").hasAuthority("")
//                        .requestMatchers("/user").authenticated()
                                .requestMatchers("/contact", "/notices", "/error", "/register", "/invalidUrl").permitAll()
                );

//        http.csrf(AbstractHttpConfigurer::disable);

        http.formLogin(withDefaults());
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}
