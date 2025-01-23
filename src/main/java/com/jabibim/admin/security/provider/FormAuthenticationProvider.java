package com.jabibim.admin.security.provider;

import com.jabibim.admin.security.dto.AccountContext;
import com.jabibim.admin.security.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component("authenticationProvider")
@RequiredArgsConstructor
public class FormAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    @Value("${admin.password}")
    private String adminPassword;
    private static final String[] ADMIN_EMAILS = { "imsgchang97@gmail.com", "rhdms085@gmail.com", "0402gewon@gmail.com",
            "salbul.mo91@gmail.com", "bitnalchan92@gmail.com" };

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AccountContext accountContext = null;
        String loginEmail = authentication.getName();
        String password = (String) authentication.getCredentials();

        boolean isAdmin = Arrays.asList(ADMIN_EMAILS).contains(loginEmail);

        if (isAdmin) {
            if (adminPassword == null || adminPassword.isEmpty()) {
                throw new IllegalStateException("환경변수 어드민 비밀번호를 확인하세요.");
            }
            AccountDto accountDto = new AccountDto();
            accountDto.setId("ADMIN");
            accountDto.setUsername("ADMIN");
            accountDto.setAcademyId("ADMIN");
            accountDto.setRoles("ROLE_ADMIN");
            accountDto.setName("ADMIN");
            accountDto.setEmail(loginEmail);
            accountDto.setPassword(passwordEncoder.encode(adminPassword));

            if (!passwordEncoder.matches(password, accountDto.getPassword())) {
                throw new BadCredentialsException("Invalid Password");
            }

            List<GrantedAuthority> authorities = List.of(() -> "ROLE_ADMIN");

            accountContext = new AccountContext(accountDto, authorities);
        } else {
            accountContext = (AccountContext) userDetailsService.loadUserByUsername(loginEmail);

            if (!passwordEncoder.matches(password, accountContext.getAccountDto().getPassword())) {
                throw new BadCredentialsException("Invalid Password");
            }
        }

        // 비밀번호는 보안상 넘기지 않습니다.
        return new UsernamePasswordAuthenticationToken(accountContext.getAccountDto(), null,
                accountContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 전달된 authentication 객체가 UsernamePasswordAuthenticationToken 타입인지 확인 => 맞으면!
        // authenticate() 메소드 호출하겠다는 의미!
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
