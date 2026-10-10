package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 스프링 설정 클래스 등록
@EnableWebSecurity // 스프링 시큐리티 활성화
@EnableMethodSecurity // [6주차] 메서드 보안 활성화 : @PreAuthorize 사용
public class SecurityConfig {

    @Bean // 비밀번호 암호화 객체 등록 (BCrypt 해시)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // 보안 필터 체인 등록
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth // 1. URL 접근 규칙 (인가)
                .requestMatchers("/", "/hello", "/detailed_web.html",
                                 "/login", "/signup", "/error").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN") // [6주차] 관리자만
                .requestMatchers("/testdb", "/testdb/**").hasAnyRole("ADMIN", "MANAGER") // [6주차] 관리자, 매니저만
                .anyRequest().authenticated())                 // 나머지: 로그인 필요

            .formLogin(form -> form // 2. 폼 로그인 설정 (인증)
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error")
                .permitAll())

            .logout(logout -> logout // 3. 로그아웃 설정
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me"))

            .rememberMe(remember -> remember // 4. 로그인 상태 유지
                .key("비밀키")
                .tokenValiditySeconds(7 * 24 * 60 * 60));

        return http.build();
    }
}