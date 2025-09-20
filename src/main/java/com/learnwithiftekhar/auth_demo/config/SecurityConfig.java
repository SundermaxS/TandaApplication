package com.learnwithiftekhar.auth_demo.config;

import com.learnwithiftekhar.auth_demo.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF отключаем для Postman и REST API
                .csrf(csrf -> csrf.disable())

                // Настройка авторизации
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // регистрация/верификация открыты
                        .anyRequest().authenticated() // всё остальное требует авторизации
                )

                // Отключаем форму логина
                .formLogin(form -> form.disable())

                // Включаем базовую HTTP-авторизацию (для API удобно)
                .httpBasic(withDefaults());

        return http.build();
    }

}
