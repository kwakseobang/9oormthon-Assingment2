package com.kwakmunsu.diary.global.jwt.config;

import static com.kwakmunsu.diary.member.entity.Role.ADMIN;
import static com.kwakmunsu.diary.member.entity.Role.MEMBER;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final String[] adminUrl = {"/admin/**"};
    private final String[] permitAllUrl = {"/", "/error", "/auth/**"};
    private final String[] hasRoleUrl = {"/diaries/**", "/members/**"};

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(permitAllUrl).permitAll()
                        .requestMatchers(adminUrl).hasRole(ADMIN.name())
                        .requestMatchers(hasRoleUrl).hasAnyRole(ADMIN.name(), MEMBER.name())
                        .anyRequest().authenticated());

        return http.build();
    }

}
