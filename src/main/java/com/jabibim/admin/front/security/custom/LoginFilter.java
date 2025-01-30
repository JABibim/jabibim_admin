package com.jabibim.admin.front.security.custom;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jabibim.admin.dto.StudentUserVO;
import com.jabibim.admin.front.dto.LoginRequest;
import com.jabibim.admin.service.LoginHistoryService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
  // 커스텀 UsernamePassword 필터

  private final JwtTokenProvider jwtTokenProvider;
  private final ObjectMapper objectMapper;
  private final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

  private final LoginHistoryService loginHistoryService;

  public LoginFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, LoginHistoryService loginHistoryService  ) {
    logger.info("LoginFilter constructor called");
    super.setAuthenticationManager(authenticationManager);
    this.jwtTokenProvider = jwtTokenProvider;
    this.objectMapper = new ObjectMapper();
    setFilterProcessesUrl("/api/auth/login");
    this.loginHistoryService = loginHistoryService;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {
    logger.info("Attempting authentication for request: {}", request.getRequestURI());
    logger.debug("Request method: {}", request.getMethod());
    logger.debug("Content type: {}", request.getContentType());

    try {
      LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

      logger.debug("Parsed login request for email: {}", loginRequest.getEmail());
      logger.debug("Academy ID from request: {}", loginRequest.getAcademyId());

      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest,
          loginRequest.getPassword());

      logger.info("Created authentication token, proceeding with authentication");
      return this.getAuthenticationManager().authenticate(authToken);
    } catch (IOException e) {
      logger.error("Failed to parse authentication request", e);
      throw new AuthenticationServiceException("Failed to parse authentication request", e);
    }
  }

  // 로그인 성공 시 jwt 발급 액세스 + 리프레시
  @Override
  protected void successfulAuthentication(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication auth) throws IOException {
    logger.info("Authentication successful, generating tokens");

    JwtCustomUserDetails userDetails = (JwtCustomUserDetails) auth.getPrincipal();
    StudentUserVO user = userDetails.getUser();

    logger.debug("Generating tokens for user: {}", user.getStudentEmail());

    // 2. 권한 정보 추출
    List<String> roles = auth.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    logger.debug("User roles: {}", roles);

    try {
      // 3. JWT 토큰 생성
      String accessToken = jwtTokenProvider.createToken(user, roles);
      String refreshToken = jwtTokenProvider.createRefreshToken(user);

      Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
      refreshTokenCookie.setPath("/");
      refreshTokenCookie.setHttpOnly(true);
      // refreshTokenCookie.setSecure(true);
      refreshTokenCookie.setDomain("localhost");
      refreshTokenCookie.setMaxAge(60 * 60 * 24 * 30); // 30일
      response.addCookie(refreshTokenCookie);

      logger.debug("Tokens generated successfully");

      // Set response
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding(StandardCharsets.UTF_8.name());

      // 5. 응답 바디 작성
      Map<String, Object> responseBody = new HashMap<>();
      responseBody.put("accessToken", "Bearer " + accessToken);
      responseBody.put("userId", user.getStudentId());
      responseBody.put("email", user.getStudentEmail());
      responseBody.put("academyId", user.getAcademyId());
      responseBody.put("roles", roles);
      responseBody.put("refreshToken", refreshToken);

      new ObjectMapper().writeValue(response.getWriter(), responseBody);
      logger.info("Authentication response sent successfully");
    } catch (Exception e) {
      logger.error("Error during token generation or response writing", e);
      throw new IOException("Failed to complete authentication process", e);
    }
  }

  // 로그인 실패 시 처리
  @Override
  protected void unsuccessfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException failed) throws IOException {
    logger.error("Authentication failed: {}", failed.getMessage());

    response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401 번 인증 실패 코드 반환
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("error", "Authentication failed");
    errorResponse.put("message", failed.getMessage());

    new ObjectMapper().writeValue(response.getWriter(), errorResponse);
    logger.debug("Error response sent to client");
  }

}
