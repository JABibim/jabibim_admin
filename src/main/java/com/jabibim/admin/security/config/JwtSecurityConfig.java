package com.jabibim.admin.security.config;

import com.jabibim.admin.security.customfilter.JwtAuthenticationFilter;
import com.jabibim.admin.security.customfilter.LoginFilter;
import com.jabibim.admin.security.dto.AcademyProperties;
import com.jabibim.admin.security.provider.JwtAuthenticationProvider;
import com.jabibim.admin.security.provider.JwtTokenProvider;
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

import java.util.Collections;

@Configuration
@Order(1) // 우선 순위 시큐리티 컨피그
@RequiredArgsConstructor
public class JwtSecurityConfig {

        private final Logger logger = LoggerFactory.getLogger(JwtSecurityConfig.class);
        private final JwtTokenProvider jwtTokenProvider;
        // 학원 정보
        private final AcademyProperties academyProperties;
        // AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
        private final JwtAuthenticationProvider jwtAuthenticationProvider;

        @Bean
        public AuthenticationManager authenticationManager() {
                return new ProviderManager(Collections.singletonList(jwtAuthenticationProvider));
        }


        @Bean
        public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {
                logger.info("jwtSecurityFilterChain =====>");

                LoginFilter loginFilter = new LoginFilter(authenticationManager(), jwtTokenProvider, academyProperties);
                loginFilter.setFilterProcessesUrl("/api/auth/login");

                http
                                .securityMatcher("/api/**") // API 경로만 처리

                                .formLogin((AbstractHttpConfigurer::disable)) // formLogin 필터 무효화

                                .httpBasic((AbstractHttpConfigurer::disable)) // http 기본 인증 방식인 username, password 무효화

                                .csrf(AbstractHttpConfigurer::disable) // csrf 무효화

                                .authenticationProvider(jwtAuthenticationProvider)

                                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/login", "/api/public/**")
                                                .permitAll()
                                                // "/api/auth/**" 를 통한 로그인, 회원가입 요청과
                                                // "/api/public/**" 을 통해 받는 api 엔드포인트는
                                                // 모두 허용 이후 더 추가 가능하다.
                                                .anyRequest().authenticated())
                                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                                                UsernamePasswordAuthenticationFilter.class)

                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // session
                                                                                                          // 사용하지
                                                                                                          // 않으므로
                                                                                                          // STATELESS
                                                                                                          // 로 고정

                return http.build();
        }

}
