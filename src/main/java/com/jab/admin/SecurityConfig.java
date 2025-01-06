package com.jab.admin;

import com.jab.admin.security.handler.LoginFailHandler;
import com.jab.admin.security.handler.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final AuthenticationProvider adminAuthenticationProvider;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailHandler loginFailHandler;

    public SecurityConfig(AuthenticationProvider adminAuthenticationProvider, LoginSuccessHandler loginSuccessHandler, LoginFailHandler loginFailHandler) {
        this.adminAuthenticationProvider = adminAuthenticationProvider;
        this.loginSuccessHandler = loginSuccessHandler;
        this.loginFailHandler = loginFailHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin((formLogin) -> formLogin.loginPage("/member/login")
            .loginProcessingUrl("/member/loginProcess")
            .usernameParameter("email")
            .passwordParameter("password")
            .successHandler(loginSuccessHandler)
            .failureHandler(loginFailHandler));

        return http.build(); // Spring SecurityFilterChain 객체를 반환하면 앞으로 모든 http 요청에 대해서 작동하게된다.
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public List<AuthenticationProvider> authenticationProviders() {
        // 사용할 Provider로 작성하고 아래에 넣어주면, SpringSecurity가 순차적으로 Provider를 수행
        return List.of(adminAuthenticationProvider);
    }

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }
}
