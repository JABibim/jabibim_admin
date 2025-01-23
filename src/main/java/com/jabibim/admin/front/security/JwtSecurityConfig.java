package com.jabibim.admin.front.security;

import com.jabibim.admin.front.properties.AcademyProperties;
import com.jabibim.admin.front.security.custom.JwtAuthenticationFilter;
import com.jabibim.admin.front.security.custom.JwtAuthenticationProvider;
import com.jabibim.admin.front.security.custom.JwtTokenProvider;
import com.jabibim.admin.front.security.custom.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.*;

@Configuration
@Order(1)
@RequiredArgsConstructor
public class JwtSecurityConfig {

        private final Logger logger = LoggerFactory.getLogger(JwtSecurityConfig.class);
        // jwt 토큰 발급 클래스
        private final JwtTokenProvider jwtTokenProvider;
        // AuthenticationManager가 사용할 커스텀 Provider 클래스
        private final JwtAuthenticationProvider jwtAuthenticationProvider;

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

                LoginFilter loginFilter = new LoginFilter(authenticationManager(), jwtTokenProvider);
                loginFilter.setFilterProcessesUrl("/api/auth/login");

                http
                                .securityMatcher("/api/**") // "/api/**" 경로만 처리

                                .formLogin((AbstractHttpConfigurer::disable)) // formLogin 필터 무효화

                                .httpBasic((AbstractHttpConfigurer::disable)) // http 기본 인증 방식인 username, password 무효화

                                .csrf(AbstractHttpConfigurer::disable) // csrf 무효화

                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                                .authenticationProvider(jwtAuthenticationProvider) // 커스텀 인증 클래스 설정

                                // 회원 가입, 로그인 요청 url, 외부 api 엔드포인트에 대해 보안 필터 해제
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/api/auth/login", "/api/auth/join", "/api/public/**")
                                                .permitAll()
                                                // 이후 더 추가 가능하다.
                                                .anyRequest().authenticated())
                                // 기본 UsernamePasswordFilter 자리에 커스텀 UsernamePassword 필터 대신 주입
                                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                                // 로그인 처리를 할 UsernamePassword 필터 뒤에 jwt 검사를 할 필터 추가
                                .addFilterAfter(new JwtAuthenticationFilter(jwtTokenProvider),
                                                UsernamePasswordAuthenticationFilter.class)

                                // 세션 로그인 방식을 사용하지 않으므로 요청 처리가 끝날 때 마다 session 삭제하는 STATELESS 설정
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // React 개발 서버
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
                configuration.setExposedHeaders(Arrays.asList("Authorization")); // 프론트에서 헤더 접근 허용
                configuration.setAllowCredentials(true); // credentials 허용
                configuration.setMaxAge(3600L); // preflight 캐시

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

}
