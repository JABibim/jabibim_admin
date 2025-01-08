package com.jabibim.admin.security.provider;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class AdminAuthenticationProvider implements AuthenticationProvider {
    @Value("${admin.password}")
    private String adminPassword;

    private static final String[] ADMIN_EMAILS = {"imsgchang97@gmail.com", "rhdms085@gmail.com", "0402gewon@gmail.com", "salbul.mo91@gmail.com", "bitnalchan92@gmail.com"};

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        ArrayList<String> adminEmails = new ArrayList<>(Arrays.asList(ADMIN_EMAILS));
        Optional<String> adminEmail = adminEmails.stream().filter(admin -> admin.equals(email)).findAny();

        if (adminEmail.isPresent()) {
            if (password.equals(adminPassword)) {
                // spring security에서 기본적으로 사용하는 인증 토큰
                return new UsernamePasswordAuthenticationToken(email, password, List.of(() -> "ROLE_ADMIN"));
            } else {
                // 비밀번호가 일치하지 않는 경우 던지는 예외
                throw new BadCredentialsException("관리자 계정 비밀번호를 확인해주세요.");
            }
        }

        // null을 반환한다는게 인증에 실패했다는 의미
        return new UsernamePasswordAuthenticationToken(email, password, List.of(() -> "USER"));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // override된 함수로, 현재 AuthenticationProvider가 어떤 타입의 인증을 처리하는지 명시 => 그리고 여기서는 UsernamePasswordAuthenticationToken타입으로 인증을 하고 있다.
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
