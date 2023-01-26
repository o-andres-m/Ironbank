package com.ironhack.ironbank.security;

import com.ironhack.ironbank.advice.ControllerAdvice;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JpaUserDetailService jpaUserDetailService;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeHttpRequests()

                // Admin ENDPOINTS (any method)
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // AccountHolder ENDPOINTS


                // ThirdParty ENDPOINTS (any method)
                .requestMatchers("/thirdparty/account").hasRole("THIRDPARTY")


                // Public ENDPOINTS (any method)
                .requestMatchers("/holders/register", "/thirdparty/register").permitAll()

                .requestMatchers(HttpMethod.POST,"/holders/create/accounts/**").hasRole("ACCOUNTHOLDER")

                .anyRequest()
                .authenticated()
                .and()
                .userDetailsService(jpaUserDetailService)
                .httpBasic()
                .and()
                .build();
         }



}
