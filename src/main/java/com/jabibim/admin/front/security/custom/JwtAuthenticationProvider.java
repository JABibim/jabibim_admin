package com.jabibim.admin.front.security.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.jabibim.admin.front.dto.LoginRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

  private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationProvider.class);

  private final JwtUserDetailService userDetailsService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    logger.info("Starting authentication process in JwtAuthenticationProvider");

    try {
      LoginRequest loginRequest = (LoginRequest) authentication.getPrincipal();
      logger.debug("Processing authentication for email: {}, academyId: {}",
          loginRequest.getEmail(),
          loginRequest.getAcademyId());

      UserDetails userDetails = userDetailsService.loadUserByLoginRequest(loginRequest);
      logger.debug("User details loaded successfully for email: {}", loginRequest.getEmail());

      if (userDetails == null) {
        logger.error("Authentication failed: User details not found");
        throw new BadCredentialsException("사용자를 찾을 수 없습니다.");
      }

      if (!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
        logger.error("Authentication failed: Invalid password for user: {}", loginRequest.getEmail());
        throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
      }

      logger.info("Authentication successful for user: {}", loginRequest.getEmail());
      return new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities());

    } catch (BadCredentialsException e) {
      logger.error("Authentication failed due to bad credentials: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      logger.error("Unexpected error during authentication", e);
      throw new org.springframework.security.authentication.AuthenticationServiceException("인증 처리 중 오류가 발생했습니다.", e);
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    logger.debug("Checking if provider supports authentication type: {}", authentication.getName());
    boolean supports = UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    logger.debug("Provider {} authentication type: {}",
        supports ? "supports" : "does not support",
        authentication.getName());
    return supports;
  }
}