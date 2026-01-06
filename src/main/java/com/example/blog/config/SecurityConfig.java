package com.example.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((request) -> request
                        // 1. Cho phép truy cập tự do vào các trang này:
                        .requestMatchers("/login", "/register", "/css/**", "/js/**", "/images/**", "/uploads/**").permitAll()
                        // 2. Tất cả các trang khác yêu cầu đăng nhập
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        // Cấu hình trang đăng nhập
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/", true) // Đăng nhập thành công thì về trang chủ
                        .permitAll()
                        )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout") // Đăng xuất xong về trang chủ
                        .permitAll()); // Cho phép logout
        return http.build();
    }

    // Bean này dùng để mã hóa mật khẩu (biến 123456 thành chuỗi ký tự loằng ngoằng)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
