package com.example.kidz_school.config;

import com.example.kidz_school.filter.JwtAuthenticationFilter;
import com.example.kidz_school.security.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/myAccount").authenticated()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/assets/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/loginEndpoint").permitAll()
                        .requestMatchers(HttpMethod.POST, "/loginEndpoint").permitAll()
                        .requestMatchers("/user/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic()
                .and()
                .formLogin()
                .loginPage("/loginEndpoint")
                .loginProcessingUrl("/login")
                .permitAll()
                .and()
                .sessionManagement()
                .and()
                .logout().permitAll()
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}