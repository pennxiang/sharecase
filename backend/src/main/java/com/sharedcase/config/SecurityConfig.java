package com.sharedcase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * ClassName: SecurityConfig
 * Package: com.sharedcase.config
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 16:01
 */
@Configuration
public class SecurityConfig {

    /**
     * 哈希加盐给密码加密
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
/*
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll() // ✅ 放行 swagger
                        .anyRequest().authenticated() // 其余请求需要认证
                )
                .formLogin() // ✅ 使用表单登录（如 /login 页面）
                .and()
                .httpBasic(); // ✅ 也支持 basic auth（适配 Swagger UI）

        return http.build();
    }*/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 禁用 CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 所有请求都允许
                );

        return http.build();
    }
}
