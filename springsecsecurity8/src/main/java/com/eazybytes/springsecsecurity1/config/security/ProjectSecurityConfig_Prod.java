package com.eazybytes.springsecsecurity1.config.security;

import com.eazybytes.springsecsecurity1.exceptions.CustomAccessDeniedHandler;
import com.eazybytes.springsecsecurity1.exceptions.CustomBasicAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("prod")
public class ProjectSecurityConfig_Prod {

    @Bean
    public SecurityFilterChain doFilter(HttpSecurity http) throws Exception{
        http.cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                corsConfiguration.setAllowCredentials(true);
                corsConfiguration.setMaxAge(3600l);
                return corsConfiguration;
            }
        }));
        //session management
        //if session timeout then redirect to /invalidUrl
        http.sessionManagement(smc -> smc
                .invalidSessionUrl("/invalidUrl")
                // configure the maximum number of sessions to one, by
                // default new session will replace the older one
                .maximumSessions(1)
                // this config prevents further login, by the same user
                // meaning, unless the previous session expires
                // or the user login out before a new session can be created
                .maxSessionsPreventsLogin(true)
        );
        //https configuration
        http.requiresChannel(registry -> registry.anyRequest().requiresSecure() ) //HTTPS only
                //define all routes with their permissions (secured (authenticated) or open (permit) )
                .authorizeHttpRequests(requests -> requests
                .requestMatchers("/accounts","/balance","/cards", "/loans","/user").authenticated()
                .requestMatchers("/contact", "/notices", "/error", "/register").permitAll()
        );

//        http.csrf(AbstractHttpConfigurer::disable);

        http.formLogin(withDefaults());
        http.httpBasic(hbc-> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        http.logout(lgc -> lgc.invalidateHttpSession(true).deleteCookies("JSESSIONID").clearAuthentication(true));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker(){
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

}
