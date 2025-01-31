package com.jabibim.admin.front.security.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.jabibim.admin.front.dto.LoginRequest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

  @NonNull
  private JwtUserDetailService userDetailsService;

  private final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

  @Override
  public Authentication authenticate(Authentication authentication) {
    if (authentication.getPrincipal() instanceof LoginRequest) {
      LoginRequest loginRequest = (LoginRequest) authentication.getPrincipal();

      UserDetails userDetails = userDetailsService.loadUserByLoginRequest(loginRequest);

      logger.info("CustomAuthenticationProvider " + userDetails.toString());

      return new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities());
    }
    throw new AuthenticationServiceException("인증 정보가 올바르지 않습니다.");
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return LoginRequest.class.isAssignableFrom(authentication);
  }
}