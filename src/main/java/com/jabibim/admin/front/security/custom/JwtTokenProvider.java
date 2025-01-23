package com.jabibim.admin.front.security.custom;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.jabibim.admin.dto.StudentUserVO;
import com.jabibim.admin.front.dto.LoginRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Component
public class JwtTokenProvider {
  // jwt 인증용 액세스 비밀키
  private String accessSecretKey;

  // jwt 인증용 리프레쉬 비밀키
  private String refreshSecretKey;

  // 5분 (300000)
  long accessValidityInMilliseconds;

  // 7일 (604,800,000)
  long refreshValidityInMilliseconds;

  private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

  private JwtUserDetailService jwtUserDetailService;

  public JwtTokenProvider(JwtUserDetailService jwtUserDetailService,
      @Value("${jwt.secret.access}") String accessSecretKey, @Value("${jwt.secret.refresh}") String refreshSecretKey,
      @Value("${jwt.expiration.access}") long AccessValidityInMilliseconds,
      @Value("${jwt.expiration.refresh}") long RefreshValidityInMilliseconds) {
    this.jwtUserDetailService = jwtUserDetailService;
    this.accessSecretKey = accessSecretKey;
    this.refreshSecretKey = refreshSecretKey;
    this.accessValidityInMilliseconds = AccessValidityInMilliseconds;
    this.refreshValidityInMilliseconds = RefreshValidityInMilliseconds;
  }

  // 액세스 토큰 생성
  public String createToken(StudentUserVO user, List<String> roles) {
    logger.debug("Creating access token for user: {}", user.getStudentEmail());
    String token = Jwts.builder()
        .subject(user.getStudentEmail())
        .issuedAt(Date.from(Instant.now()))
        .expiration(Date.from(Instant.now().plusMillis(accessValidityInMilliseconds)))
        .claim("roles", roles)
        .claim("studentId", user.getStudentId())
        .claim("academyId", user.getAcademyId())
        .signWith(getAccessSigningKey())
        .compact();
    logger.debug("Access token created successfully");
    return token;
  }

  // 리프레쉬 토큰 생성
  public String createRefreshToken(StudentUserVO user) {
    logger.debug("Creating refresh token for user: {}", user.getStudentEmail());
    String token = Jwts.builder()
        .subject(user.getStudentEmail())
        .issuedAt(Date.from(Instant.now()))
        .expiration(Date.from(Instant.now().plusMillis(refreshValidityInMilliseconds)))
        .claim("academyId", user.getAcademyId())
        .signWith(getRefreshSigningKey())
        .compact();
    logger.debug("Refresh token created successfully");
    return token;
  }

  // 액세스 토큰 서명 키 생성
  private SecretKey getAccessSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(accessSecretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  // 리프레쉬 토큰 서명 키 생성
  private SecretKey getRefreshSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(refreshSecretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  // 토큰 검증 (액세스 토큰 + 리프레시 토큰 처리)
  public TokenValidationResult validateToken(String token) {
    try {
      // 액세스 토큰 검증 시도
      Jwts.parser()
          .verifyWith(getAccessSigningKey())
          .build()
          .parseSignedClaims(token);
      logger.debug("Access token validated successfully");
      return new TokenValidationResult(true, TokenType.ACCESS);
    } catch (JwtException | IllegalArgumentException e) {
      logger.debug("Access token validation failed, trying refresh token");

      try {
        // 리프레시 토큰 검증 시도
        Jwts.parser()
            .verifyWith(getRefreshSigningKey())
            .build()
            .parseSignedClaims(token);
        logger.debug("Refresh token validated successfully");
        return new TokenValidationResult(true, TokenType.REFRESH);
      } catch (JwtException | IllegalArgumentException refreshEx) {
        logger.error("Both token validations failed: {}", refreshEx.getMessage());
        return new TokenValidationResult(false, null);
      }
    }
  }

  // 토큰 검증 결과를 담는 클래스
  @Getter
  @AllArgsConstructor
  public static class TokenValidationResult {
    private final boolean isValid;
    private final TokenType tokenType;
  }

  public enum TokenType {
    ACCESS, REFRESH
  }

  // 액세스 토큰에서 정보 얻기 위한 객체 반환
  public Claims getClaims(String token) {
    return Jwts.parser()
        .verifyWith(getAccessSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  // 리프레쉬 토큰에서 정보 얻기 위한 객체 반환
  public Claims getRefreshClaims(String token) {
    return Jwts.parser()
        .verifyWith(getRefreshSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public Authentication getAuthentication(String token) {
    try {
      Claims claims = getClaims(token);
      logger.debug("Extracting authentication from token for user: {}", claims.getSubject());

      String email = claims.getSubject();
      String name = claims.get("name", String.class);
      String id = claims.get("id", String.class);

      @SuppressWarnings("unchecked")
      Collection<? extends GrantedAuthority> authorities = ((List<String>) claims.get("roles"))
          .stream()
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toList());

      JwtUserDetails userDetails = JwtUserDetails.builder()
          .id(id)
          .email(email)
          .name(name)
          .authorities(authorities)
          .build();

      logger.debug("Authentication created successfully for user: {}", email);
      return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    } catch (Exception e) {
      logger.error("Failed to create authentication: {}", e.getMessage());
      throw e;
    }
  }

  // 리프레시 토큰 검증
  public boolean validateRefreshToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(getRefreshSigningKey())
          .build()
          .parseSignedClaims(token);
      logger.debug("Refresh token validated successfully");
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      logger.error("Refresh token validation failed: {}", e.getMessage());
      return false;
    }
  }

  // 리프레시 토큰으로 액세스 토큰 재발급
  public String recreateAccessToken(String refreshToken) {
    try {
      logger.debug("Attempting to recreate access token from refresh token");
      Claims refreshClaims = getRefreshClaims(refreshToken);
      String username = refreshClaims.getSubject();
      String academyId = (String) refreshClaims.get("academyId");

      JwtCustomUserDetails details = (JwtCustomUserDetails) jwtUserDetailService
          .loadUserByLoginRequest(new LoginRequest(username, academyId));

      List<String> roles = details.getAuthorities().stream()
          .map(GrantedAuthority::getAuthority)
          .collect(Collectors.toList());

      StudentUserVO user = details.getUser();

      logger.debug("Access token recreated successfully for user: {}", username);
      return createToken(user, roles);
    } catch (Exception e) {
      logger.error("Failed to recreate access token: {}", e.getMessage());
      throw e;
    }
  }

  // 액세스 토큰 재발급
  public String reissueAccessToken(String refreshToken) {
    Claims claims = getClaims(refreshToken);
    String studentId = claims.get("studentId", String.class);
    String academyId = claims.get("academyId", String.class);

    JwtCustomUserDetails user = (JwtCustomUserDetails) jwtUserDetailService.loadUserByLoginRequest(new LoginRequest(studentId, academyId));

    List<String> roles = user.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    return createToken(user.getUser(), roles);
  }

}
