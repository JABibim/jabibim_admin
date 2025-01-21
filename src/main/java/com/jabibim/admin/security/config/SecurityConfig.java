package com.jabibim.admin.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.jabibim.admin.security.handler.FormAccessDeniedHandler;
import com.jabibim.admin.security.provider.FormAuthenticationProvider;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;

@EnableWebSecurity
@Configuration
@Order(2)
@RequiredArgsConstructor
public class SecurityConfig {

        private final FormAuthenticationProvider formAuthenticationProvider;
        private final AuthenticationSuccessHandler successHandler;
        private final AuthenticationFailureHandler failureHandler;
        private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                System.out.println("=================> SecurityConfig");

                http
                    .securityMatcher(new NegatedRequestMatcher(new AntPathRequestMatcher("/api/**")))  // API 요청 제외

                                .authorizeHttpRequests(auth -> auth
                                                // 정적 자원 permitAll ( 성능 고려, security 적용 안함 )
                                                .requestMatchers("/css/**", "/img/**", "/js/**", "/niceAdmin/**",
                                                                "/sql/**")
                                                .permitAll()
                                                // login page 경로는 모두 permitAll
                                                .requestMatchers("/", "/login*").permitAll()
                                                // 권한별 접근 제한 설정
                                                .requestMatchers("/setting").hasAuthority("ROLE_ADMIN")
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                // loginPage 경로 설정
                                                .loginPage("/login").permitAll()
                                                // authenticationDetailsSource 설정 ( 부가정보 셋팅할 수 있음 )
                                                .authenticationDetailsSource(authenticationDetailsSource)
                                                // usernameParameter, passwordParameter 설정 ( name property가 email, // password인 // input 태그를 찾아서 처리 )
                                                .usernameParameter("email")
                                                .passwordParameter("password")
                                                // 성공 처리 Handler
                                                .successHandler(successHandler)
                                                // 실패 처리 Handler
                                                .failureHandler(failureHandler))
                                // authenticationProvider 설정
                                // DB에서 인증에 필요한 데이터를 가져다 비교하는 커스텀 Provider 객체를 AuthenticationManager에게 사용하도록 설정
                                .authenticationProvider(formAuthenticationProvider)
                                // exceptionHandling 설정 ( 인증 받은 상태에서 호출되는거! 로그인한 사용자가 접근할 수 없는 페이지에 접근했을 때 )
                                .exceptionHandling(exception -> exception
                                                .accessDeniedHandler(new FormAccessDeniedHandler("/denied")));

                http.logout((logout) -> logout
                                .logoutSuccessUrl("/login")
                                .logoutUrl("/logout")
                                .invalidateHttpSession(true)
                                .deleteCookies("remember-me", "JSESSION_ID"));

                return http.build();
        }
}
