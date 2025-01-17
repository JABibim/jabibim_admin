package com.jabibim.admin.security.provider;

import com.jabibim.admin.dto.StudentUserVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

  @Value("${jwt.secret}")
  private String secretKey;
  // 1시간 (3600000)
  @Value("${jwt.expiration}")
  long validityInMilliseconds;

  public String createToken(StudentUserVO user, List<String> roles) {

    // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    return Jwts.builder()
        .subject(user.getStudentEmail())
        .issuedAt(Date.from(Instant.now())) // 현재 시간
        .expiration(Date.from(Instant.now().plusMillis(validityInMilliseconds))) // 현재 시간 + 1시간
        // 추가 정보
        .claim("roles", roles) // 권한
        .claim("studentId", user.getStudentId())
        .claim("academyId", user.getAcademyId())
        // .claim("metadata", Map.of( // 추가 정보로 map 형태를 담는데 최대 10개.
        // // 필요없는 정보는 지우자. 많으면 서버에 부담이다.
        // "studentId", user.getStudentId(),
        // "createdAt", sdf.format(user.getCreatedAt()),
        // "studentName", user.getStudentName(),
        // "studentImgName", user.getStudentImgName(),
        // "studentImgOrigin", user.getStudentImgOrigin(),
        // "verification", user.getVerification(),
        // "adsAgreed", user.getAdsAgreed(),
        // "academyId", user.getAcademyId(),
        // "grade", user.getGradeId(),
        // "studentAddress", user.getStudentAddress().toString()
        // ))
        .signWith(getSigningKey())
        .compact();
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public Claims getClaims(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public Authentication getAuthentication(String token) {
    Claims claims = getClaims(token);

    // 권한 정보 추출
    @SuppressWarnings("unchecked")
    Collection<? extends GrantedAuthority> authorities = ((List<String>) claims.get("roles")).stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());

    // UserDetails 객체 만들어서 Authentication 리턴
    UserDetails principal = new User(claims.getSubject(), "", authorities);

    return new UsernamePasswordAuthenticationToken(principal, token, authorities);
  }
}
