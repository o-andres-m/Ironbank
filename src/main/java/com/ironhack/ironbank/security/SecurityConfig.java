package com.ironhack.ironbank.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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

                // Admin ENDPOINTS
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // AccountHolder ENDPOINTS
                    // Public
                .requestMatchers("/holders/register", "/holders/forgotpassword").permitAll()
                    // Login
                .requestMatchers("/holders/**").hasRole("ACCOUNTHOLDER")


                // ThirdParty ENDPOINTS
                .requestMatchers("/thirdparty/account**").hasRole("THIRDPARTY")


                // Public ENDPOINTS (any method)
                .requestMatchers("/holders/register", "/thirdparty/register").permitAll()
                .requestMatchers("/thirdparty/chargeservice","/thirdparty/transfer" ).permitAll()

                // Auto ENDPOINTS (FreeAccess)
                .requestMatchers("/auto/**").permitAll()



                .anyRequest()
                .authenticated()
                .and()
                .userDetailsService(jpaUserDetailService)
                .httpBasic()
                .and()
                .build();
         }



}
