package com.jabibim.admin.front.security;

import java.util.Arrays;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.jabibim.admin.front.security.custom.JwtAuthenticationFilter;
import com.jabibim.admin.front.security.custom.JwtAuthenticationProvider;
import com.jabibim.admin.front.security.custom.JwtTokenProvider;
import com.jabibim.admin.front.security.custom.LoginFilter;
import com.jabibim.admin.service.LoginHistoryService;
import com.jabibim.admin.service.RedisService;
import com.jabibim.admin.service.StudentService;

import lombok.RequiredArgsConstructor;

@Configuration
@Order(1)
@RequiredArgsConstructor
public class JwtSecurityConfig {
    @Value("${front.host}")
    private String frontHost;

    private final Logger logger = LoggerFactory.getLogger(JwtSecurityConfig.class);
    // jwt 토큰 발급 클래스
    private final JwtTokenProvider jwtTokenProvider;
    // AuthenticationManager가 사용할 커스텀 Provider 클래스
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final LoginHistoryService loginHistoryService;

    private final StudentService studentService;
    private final RedisService redisService;

    @Bean
    public AuthenticationManager authenticationManager() {
        // 로그인 처리 과정에서
        // 세션 로그인에 사용되는 default AuthenticationManager 를 호출하여
        // teacher 테이블을 조회하여 인증하는 세션 로그인용 Provider 객체가 사용되므로
        // 수동으로 AuthenticationManager 객체를 생성하여
        // student 테이블을 조회하여 인증하는 학생 로그인용 Provider 객체를 주입한다.
        return new ProviderManager(Collections.singletonList(jwtAuthenticationProvider));
    }

    @Bean
    public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {
        logger.info("jwtSecurityFilterChain =====>");

        LoginFilter loginFilter = new LoginFilter(authenticationManager(), jwtTokenProvider, loginHistoryService,
                studentService);
        loginFilter.setFilterProcessesUrl("/api/auth/login");

        http
                .securityMatcher("/api/**") // "/api/**" 경로만 처리
                .formLogin((AbstractHttpConfigurer::disable))
                .httpBasic((AbstractHttpConfigurer::disable))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/auth/join", "/api/public/**", "/api/webhook/**", "/webhook/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .authenticationProvider(jwtAuthenticationProvider)

                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtAuthenticationFilter(jwtTokenProvider, redisService),
                        UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin(frontHost); // 프론트엔드
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true); // 인증 정보 허용
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Set-Cookie")); // 프론트엔드에서 접근 가능한 헤더
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
