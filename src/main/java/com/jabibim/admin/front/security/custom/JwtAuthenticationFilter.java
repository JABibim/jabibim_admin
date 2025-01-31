package com.jabibim.admin.front.security.custom;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jabibim.admin.dto.StudentUserVO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  // jwt 토큰 유효성 검사하는 필터 요청이 올 때마다 한 번씩 실행된다.

  private final JwtTokenProvider jwtTokenProvider;
  private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    return path.equals("/api/auth/login") ||
        path.equals("/api/auth/join") ||
        path.startsWith("/api/public/") ||
        path.startsWith("/api/webhook/") ||
        path.startsWith("/webhook/");

  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    logger.info("▶▶▶ JWT Authentication Filter START - URI: {}", request.getRequestURI());

    if (request.getRequestURI().startsWith("api/webhook/") || request.getRequestURI().startsWith("webhook/")) {
      filterChain.doFilter(request, response);
    }

    try {
      // 1. 토큰 추출 단계
      String token = getJwtFromRequest(request);
      logger.debug("1. Token extraction result: {}", token != null ? "[REDACTED]" : "NOT FOUND");

      if (!StringUtils.hasText(token)) {
        logger.warn("1.1 No JWT token found in request headers");
        handleMissingToken(response);
        return;
      }

      // 2. 액세스 토큰 검증 단계
      logger.info("2. Access token validation: {}", jwtTokenProvider.validateToken(token).isValid() ? "VALID" : "INVALID");

      if (jwtTokenProvider.validateToken(token).isValid()) {
        logger.debug("2.1 Processing valid access token");
        processValidToken(token);
        logger.info("2.2 Security context updated for user: {}",
            SecurityContextHolder.getContext().getAuthentication().getName());
        filterChain.doFilter(request, response);
        return;
      }

      // 3. 리프레시 토큰 처리 단계
      logger.info("3. Starting refresh token processing");
      String refreshToken = getRefreshTokenFromCookie(request);
      logger.debug("3.1 Refresh token presence: {}", refreshToken != null ? "FOUND" : "NOT FOUND");

      if (!StringUtils.hasText(refreshToken)) {
        logger.warn("3.2 No refresh token in cookies");
        handleInvalidToken(response);
        return;
      }

      // 4. 리프레시 토큰 검증 및 새 토큰 발급
      logger.debug("4. Validating refresh token");
      if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
        logger.info("4.1 Refresh token validation SUCCESS");

        String newAccessToken = jwtTokenProvider.recreateAccessToken(refreshToken);
        logger.debug("4.2 New access token generated: [REDACTED]");

        processValidToken(newAccessToken);
        logger.info("4.3 Security context updated with new token for user: {}",
            SecurityContextHolder.getContext().getAuthentication().getName());

        response.setHeader("Authorization", "Bearer " + newAccessToken);
        logger.debug("4.4 New access token added to response headers");

        checkAndRenewRefreshToken(response, refreshToken,
            SecurityContextHolder.getContext().getAuthentication());

        filterChain.doFilter(request, response);
        return;
      }

      logger.warn("5. Both access and refresh tokens are invalid");
      handleInvalidToken(response);

    } catch (JwtException ex) {
      logger.error("!! Authentication Error: {}", ex.getMessage(), ex);
      handleAuthenticationError(response, ex);
    } catch (Exception ex) {
      logger.error("!! Unexpected Error: {}", ex.getMessage(), ex);
      handleAuthenticationError(response, ex);
    } finally {
      logger.info("◀◀◀ JWT Authentication Filter END - Status: {}", response.getStatus());
    }
  }

  private void handleMissingToken(HttpServletResponse response) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("error", "need access token.");

    new ObjectMapper().writeValue(response.getWriter(), errorResponse);
  }

  // 유효한 토큰 처리
  private void processValidToken(String token) {

    SecurityContext context = null;

    try {

      Authentication auth = jwtTokenProvider.getAuthentication(token);

      context = SecurityContextHolder.createEmptyContext();
      context.setAuthentication(auth);

      synchronized (this) {
        SecurityContextHolder.setContext(context);
      }

      logger.debug("Authentication set successfully for user: {}", auth.getName());

    } catch (Exception e) {
      if (context != null) {
        context.setAuthentication(null);
      }
      SecurityContextHolder.clearContext();
      logger.error("Failed to set authentication: {}", e.getMessage());
      throw e;
    }
  }

  // Request 헤더로부터 jwt 토큰 가져오기
  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  // 쿠키로부터 리프레쉬 토큰 가져오기
  // 쿠키에 저장하는 이유는 서버가 STATELESS 상태를 유지하기 위해서
  // 유저에게 쿠키로 전달하고 JavaScript 등으로 조작하지 못하도록
  // HttpOnly 보안 설정을 해준다.
  // https 프로토콜 사용시 secure 옵션 활성화 해야 한다.
  private String getRefreshTokenFromCookie(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("refreshToken")) {
          return cookie.getValue();
        }
      }
    }
    return null; // 빈 문자열 대신 null 반환
  }

  // Jwt 토큰 검증 과정 중 오류 생겼을 때 예외 처리
  private void handleAuthenticationError(HttpServletResponse response, Exception e) throws IOException {
    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()); // 500 번 서버 오류 코드 반환
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("message", "Error occurred during authentication.");
    new ObjectMapper().writeValue(response.getWriter(), responseBody);
  }

  // 변조된 리프레쉬 토큰 예외처리 핸들러
  private void handleInvalidToken(HttpServletResponse response) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("status", "error");
    responseBody.put("message", "인증이 만료되었습니다. 다시 로그인해주세요.");

    new ObjectMapper().writeValue(response.getWriter(), responseBody);
  }

  // 리프레쉬 토큰이 만료일이 다가오는지 체크하고 재발급
  private void checkAndRenewRefreshToken(HttpServletResponse response, String refreshToken, Authentication auth) {
    if (isRefreshTokenNearExpiration(refreshToken)) {
      String newRefreshToken = createNewRefreshToken(auth);
      setRefreshTokenCookie(response, newRefreshToken);
    }
  }

  // 리프레쉬 토큰이 만료일이 다가오는지 체크
  private boolean isRefreshTokenNearExpiration(String refreshToken) {
    try {
      Claims claims = jwtTokenProvider.getRefreshClaims(refreshToken); // getClaims 대신 getRefreshClaims 사용
      Date expiration = claims.getExpiration();

      // 만료 1일 전이면 true, 아니면 false
      return expiration.getTime() < System.currentTimeMillis() + 1000 * 60 * 60 * 24;
    } catch (Exception e) {
      logger.error("리프레시 토큰 만료 체크 중 오류 발생: {}", e.getMessage());
      return false; // 오류 발생시 만료되지 않은 것으로 처리
    }
  }

  // 리프레쉬 토큰 재발급
  private String createNewRefreshToken(Authentication auth) {
    JwtCustomUserDetails user = (JwtCustomUserDetails) auth.getPrincipal();
    StudentUserVO studentUser = user.getUser();
    return jwtTokenProvider.createRefreshToken(studentUser);
  }

  // 리프레쉬 토큰 쿠키 설정
  private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
    Cookie cookie = new Cookie("refreshToken", refreshToken);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setAttribute("SameSite", "None"); // CORS 요청에서 쿠키 전송 허용
    cookie.setMaxAge(60 * 60 * 24 * 7); // 7일
    response.addCookie(cookie);

    // SameSite 속성을 위한 헤더 직접 설정
    String cookieHeader = String.format("%s=%s; Max-Age=%d; Path=%s; HttpOnly; Secure; SameSite=None",
        cookie.getName(),
        cookie.getValue(),
        cookie.getMaxAge(),
        cookie.getPath());
    response.setHeader("Set-Cookie", cookieHeader);
  }

}
