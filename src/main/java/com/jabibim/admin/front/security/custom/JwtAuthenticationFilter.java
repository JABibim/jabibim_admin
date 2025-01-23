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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jabibim.admin.dto.StudentUserVO;
import com.jabibim.admin.front.security.custom.JwtTokenProvider.TokenValidationResult;

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
        path.startsWith("/api/public/");
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      // Authorization 헤더에서 액세스 토큰 추출
      // jwt 액세스 토큰을 가져와서 jwtTokenProvider 에서 유효성 검사 및
      // 통과 시 SecurityContextHolder 에 유저 정보 저장
      String token = getJwtFromRequest(request);

      // 토큰이 없는 경우
      if (!StringUtils.hasText(token)) {
        handleMissingToken(response);
        return; // 중요: 필터 체인 종료
      }

      // 토큰 검증
      TokenValidationResult result = jwtTokenProvider.validateToken(token);
      
      if (result.isValid()) {
        processValidToken(token);
        filterChain.doFilter(request, response);
        return;
      } else if (jwtTokenProvider.validateRefreshToken(token)) {
        processRefreshToken(request, response);
        filterChain.doFilter(request, response);
        return;
      }

    } catch (Exception e) {
      logger.error("JWT 토큰 처리 중 오류 발생", e);
      handleAuthenticationError(response, e);
      // 에러 응답 후 필터 체인 종료
    }
  }

  // 유효한 토큰 처리
  private void processValidToken(String token) {
    Authentication auth = jwtTokenProvider.getAuthentication(token);
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  // 리프레쉬 토큰 처리
  private void processRefreshToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String refreshToken = getRefreshTokenFromCookie(request);

    if (!StringUtils.hasText(refreshToken)) {
      handleMissingToken(response);
      return;
    }

    try {
      if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
        String newToken = jwtTokenProvider.recreateAccessToken(refreshToken);
        Authentication auth = jwtTokenProvider.getAuthentication(newToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        response.setHeader("Authorization", "Bearer " + newToken);

        checkAndRenewRefreshToken(response, refreshToken, auth);
      } else {
        handleExpiredRefreshToken(response);
      }
    } catch (JwtException e) {
      handleInvalidRefreshToken(response);
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
        if (cookie.getName().equals("refresh")) {
          return cookie.getValue();
        }
      }
    }
    return "";
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
  private void handleInvalidRefreshToken(HttpServletResponse response) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401 번 인증 실패 코드 반환
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    // 쿠키에 저장되어 있던 리프레쉬 토큰 삭제
    Cookie cookie = new Cookie("refresh", null);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    response.addCookie(cookie);

    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("message", "Invalid Refresh Token");
    new ObjectMapper().writeValue(response.getWriter(), responseBody);

  }

  // 만료된 리프레쉬 토큰 예외처리 핸들러
  private void handleExpiredRefreshToken(HttpServletResponse response) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401 번 인증 실패 코드 반환
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("message", "Expired Refresh Token");

    new ObjectMapper().writeValue(response.getWriter(), responseBody);
  }

  // 액세스 토큰이 없는 경우
  private void handleMissingToken(HttpServletResponse response) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("status", "error");
    responseBody.put("message", "인증 토큰이 필요합니다.");

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
    Claims claims = jwtTokenProvider.getClaims(refreshToken);
    Date expiration = claims.getExpiration();

    // 만료 1일 전이면 true, 아니면 false
    return expiration.getTime() < System.currentTimeMillis() + 1000 * 60 * 60 * 24;
  }

  // 리프레쉬 토큰 재발급
  private String createNewRefreshToken(Authentication auth) {
    JwtCustomUserDetails user = (JwtCustomUserDetails) auth.getPrincipal();
    StudentUserVO studentUser = user.getUser();
    return jwtTokenProvider.createRefreshToken(studentUser);
  }

  // 리프레쉬 토큰 쿠키 설정
  private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
    Cookie cookie = new Cookie("refresh", refreshToken);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(60 * 60 * 24 * 7); // 7일
    cookie.setPath("/");
    response.addCookie(cookie);
  }

}
