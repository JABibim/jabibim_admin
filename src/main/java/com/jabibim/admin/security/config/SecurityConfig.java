package com.jabibim.admin.security.config;

import com.jabibim.admin.oauth2.CustomClientRegistrationRepo;
import com.jabibim.admin.oauth2.CustomOAuth2AuthorizedClientService;
import com.jabibim.admin.security.handler.FormAccessDeniedHandler;
import com.jabibim.admin.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomClientRegistrationRepo customClientRegistrationRepo;
    private final CustomOAuth2AuthorizedClientService customOAuth2AuthorizedClientService;
    private final JdbcTemplate jdbcTemplate;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin((login) -> login.loginPage("/login"));

        http
                .oauth2Login((oauth2) -> oauth2
                        .loginPage("/login")
                        .clientRegistrationRepository(customClientRegistrationRepo.clientRegistrationRepository())
                        .authorizedClientService(customOAuth2AuthorizedClientService.oAuth2AuthorizedClientService(jdbcTemplate, customClientRegistrationRepo.clientRegistrationRepository()))
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(successHandler));

        http

                .authorizeHttpRequests(auth -> auth
                        // 정적 자원 permitAll ( 성능 고려, security 적용 안함 )
                        .requestMatchers("/css/**", "/img/**", "/js/**", "/niceAdmin/**", "/sql/**").permitAll()
                        // login page 경로는 모두 permitAll
                        .requestMatchers("/", "/login*","/oauth2/**" ).permitAll()
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
                .exceptionHandling(exception -> exception.accessDeniedHandler(new FormAccessDeniedHandler("/denied")));

        http.logout((logout) -> logout
                .logoutSuccessUrl("/login")
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("remember-me", "JSESSION_ID"));

        return http.build();
    }
}
