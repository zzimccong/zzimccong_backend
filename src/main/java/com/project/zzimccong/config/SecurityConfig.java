package com.project.zzimccong.config;

import com.project.zzimccong.security.jwt.JwtAuthenticationFilter;
import com.project.zzimccong.security.jwt.JwtTokenUtil;
import com.project.zzimccong.security.service.corp.CustomCorpDetailsService;
import com.project.zzimccong.security.service.user.CustomUserDetailsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtTokenUtil jwtTokenUtil;
    private final CustomCorpDetailsService corpDetailsService;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtTokenUtil jwtTokenUtil, CustomCorpDetailsService corpDetailsService, CustomUserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.corpDetailsService = corpDetailsService;
        this.userDetailsService = userDetailsService;
    }

    // CORS 설정
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                // 모든 경로에 대해 http://localhost:3000에서 오는 요청을 허용
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000", "http://10.10.10.227:3000", "http://ec2-15-168-10-98.ap-northeast-3.compute.amazonaws.com")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedMethods("*")
                        .allowCredentials(true);
            }
        };
    }

    // Spring Security 필터 체인을 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // HTTP 요청에 대한 보안 설정을 정의
                .authorizeHttpRequests(auth ->
                        auth.antMatchers("/**", "/api/auth/**").permitAll() // 모든 경로와 회원가입 및 로그인 요청을 허용
                                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 모든 OPTIONS 요청을 허용
                                .anyRequest().authenticated()) // 나머지 모든 요청은 인증 필요함
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호를 비활성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 관리를 Stateless 방식으로 설정함
                // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 전에 추가
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtil, corpDetailsService, userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // PasswordEncoder를 빈으로 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager를 빈으로 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
