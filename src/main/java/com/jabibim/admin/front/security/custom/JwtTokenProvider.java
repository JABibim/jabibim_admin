package com.jabibim.admin.front.security.custom;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

@Component
@PropertySource("classpath:properties/jwt.properties")
public class JwtTokenProvider {
  // jwt 인증용 액세스 비밀키
  @Value("${jwt.secret.access}")
  private String accessSecretKey;

  // jwt 인증용 리프레쉬 비밀키
  @Value("${jwt.secret.refresh}")
  private String refreshSecretKey;

  private SecretKey cachedAccessKey;
  private SecretKey cachedRefreshKey;

  // 10초 (10000)
  @Value("${jwt.expiration.access}")
  private long accessValidityInMilliseconds;

  // 7일 (604,800,000)
  @Value("${jwt.expiration.refresh}")
  private long refreshValidityInMilliseconds;

  private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

  private JwtUserDetailService jwtUserDetailService;

  // 토큰 생성 시 사용할 키 캐싱
  // 객체 생성 후 초기화 함수 호출
  @PostConstruct
  public void init() {
    this.cachedAccessKey = Keys.hmacShaKeyFor(accessSecretKey.getBytes(StandardCharsets.UTF_8));
    this.cachedRefreshKey = Keys.hmacShaKeyFor(refreshSecretKey.getBytes(StandardCharsets.UTF_8));
  }

  public JwtTokenProvider(JwtUserDetailService jwtUserDetailService) {
    this.jwtUserDetailService = jwtUserDetailService;
  }

  // 액세스 토큰 생성
  public String createToken(StudentUserVO user, List<String> roles) {
    logger.debug("Creating access token for user: {}", user.getStudentEmail());
    String token = Jwts.builder()
        .subject(user.getStudentEmail())
        .issuedAt(Date.from(Instant.now()))
        .expiration(Date.from(Instant.now().plusMillis(accessValidityInMilliseconds))) // 10초
        .claim("roles", roles)
        .claim("studentId", user.getStudentId())
        .claim("academyId", user.getAcademyId())
        .signWith(cachedAccessKey)
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
        .expiration(Date.from(Instant.now().plusMillis(refreshValidityInMilliseconds))) // 7일
        .claim("academyId", user.getAcademyId())
        .signWith(cachedRefreshKey)
        .compact();
    logger.debug("Refresh token created successfully");
    return token;
  }

  // 토큰 검증 (액세스 토큰 + 리프레시 토큰 처리)
  public TokenValidationResult validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(cachedAccessKey)
          .build()
          .parseSignedClaims(token);
      return new TokenValidationResult(true, TokenType.ACCESS);
    } catch (JwtException e) {
      logger.debug("Access token validation failed: {}", e.getMessage());
      return new TokenValidationResult(false, TokenType.ACCESS);
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

  // 리프레시 토큰으로 액세스 토큰 재발급
  public String recreateAccessToken(String refreshToken) {
    try {
      Claims refreshClaims = getRefreshClaims(refreshToken);
      String email = refreshClaims.getSubject();
      String academyId = refreshClaims.get("academyId", String.class);

      // DB에서 사용자 정보를 조회하여 필요한 클레임 정보를 가져옴
      JwtCustomUserDetails details = (JwtCustomUserDetails) jwtUserDetailService
          .loadUserByLoginRequest(new LoginRequest(email, academyId));

      StudentUserVO user = details.getUser();

      return Jwts.builder()
          .subject(user.getStudentEmail())
          .claim("roles", Collections.singletonList(user.getAuthRole()))
          .claim("studentId", user.getStudentId())
          .claim("academyId", user.getAcademyId())
          .issuedAt(Date.from(Instant.now()))
          .expiration(Date.from(Instant.now().plusMillis(accessValidityInMilliseconds)))
          .signWith(cachedAccessKey)
          .compact();
    } catch (Exception e) {
      logger.error("Failed to recreate access token: {}", e.getMessage());
      throw new JwtException("Invalid refresh token");
    }
  }

  // 리프레시 토큰 검증
  public boolean validateRefreshToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(cachedRefreshKey)
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (JwtException e) {
      logger.error("Refresh token validation failed: {}", e.getMessage());
      return false;
    }
  }

  // 리프레쉬 토큰에서 정보 얻기 위한 객체 반환
  public Claims getRefreshClaims(String token) {
    try {
      return Jwts.parser()
          .verifyWith(cachedRefreshKey)
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (JwtException e) {
      logger.error("Failed to get refresh claims: {}", e.getMessage());
      throw new JwtException("Invalid refresh token");
    }
  }

  // 액세스 토큰에서 정보 얻기 위한 객체 반환
  public Claims getClaims(String token) {
    try {
      return Jwts.parser()
          .verifyWith(cachedAccessKey)
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (JwtException e) {
      logger.error("Failed to get access claims: {}", e.getMessage());
      throw new JwtException("Invalid access token");
    }
  }

  public Authentication getAuthentication(String token) {
    try {
      Claims claims = getClaims(token);
      logger.debug("Extracting authentication from token for user: {}", claims.getSubject());

      String email = claims.getSubject();
      String academyId = claims.get("academyId", String.class);

      @SuppressWarnings("unchecked")
      Collection<? extends GrantedAuthority> authorities = ((List<String>) claims.get("roles"))
          .stream()
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toList());

      JwtCustomUserDetails userDetails = (JwtCustomUserDetails) jwtUserDetailService
          .loadUserByLoginRequest(new LoginRequest(email, academyId));

      logger.debug("Authentication created successfully for user: {}", email);
      return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    } catch (Exception e) {
      logger.error("Failed to create authentication: {}", e.getMessage());
      throw e;
    }
  }

  // 액세스 토큰 재발급
  public String reissueAccessToken(String refreshToken) {
    try {
      Claims claims = getRefreshClaims(refreshToken); // getClaims 대신 getRefreshClaims 사용
      String email = claims.getSubject(); // studentId 대신 email 사용
      String academyId = claims.get("academyId", String.class);

      JwtCustomUserDetails user = (JwtCustomUserDetails) jwtUserDetailService
          .loadUserByLoginRequest(new LoginRequest(email, academyId));

      List<String> roles = user.getAuthorities().stream()
          .map(GrantedAuthority::getAuthority)
          .collect(Collectors.toList());

      return createToken(user.getUser(), roles);
    } catch (Exception e) {
      logger.error("Failed to reissue access token: {}", e.getMessage());
      throw new JwtException("Failed to reissue access token", e);
    }
  }

}
