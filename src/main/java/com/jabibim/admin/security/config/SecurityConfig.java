package com.jabibim.admin.security.config;

import com.jabibim.admin.security.handler.FormAccessDeniedHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // 정적 자원 permitAll ( 성능 고려, security 적용 안함 )
                        .requestMatchers("/css/**", "/img/**", "/js/**", "/niceAdmin/**", "/sql/**").permitAll()
                        // login page 경로는 모두 permitAll
                        .requestMatchers("/", "/login*").permitAll()
                        // 웹훅 EndPoint 접근 제한 해제
                        .requestMatchers("/api/public/**", "/webhook/**").permitAll()
                        // 권한별 접근 제한 설정
                        .requestMatchers("/setting").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        // loginPage 경로 설정
                        .loginPage("/login").permitAll()
                        // authenticationDetailsSource 설정 ( 부가정보 셋팅할 수 있음 )
                        .authenticationDetailsSource(authenticationDetailsSource)
                        // usernameParameter, passwordParameter 설정 ( name property가 email, password인 input 태그를 찾아서 처리 )
                        .usernameParameter("email")
                        .passwordParameter("password")
                        // 성공 처리 Handler
                        .successHandler(successHandler)
                        // 실패 처리 Handler
                        .failureHandler(failureHandler))
                // authenticationProvider 설정
                .authenticationProvider(authenticationProvider)
                // exceptionHandling 설정 ( 인증 받은 상태에서 호출되는거! 로그인한 사용자가 접근할 수 없는 페이지에 접근했을 때 )
                .exceptionHandling(exception -> exception.accessDeniedHandler(new FormAccessDeniedHandler("/denied")))
        ;

        http.logout((logout) -> logout
                .logoutSuccessUrl("/login")
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("remember-me", "JSESSION_ID"));



        // 해당 URL 에 대해 csrf 무시.
        http
            .csrf(
                (csrfConfig) ->
                    // 해당 URL 에서 오는 post 요청에 대해 csrf 토큰 제한 풀기
                    csrfConfig.ignoringRequestMatchers("/api/public/**", "/webhook/**")
                        // api 요청에 대해 csrf 토큰 발행
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            );

        return http.build();
    }
}
