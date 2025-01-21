package com.jabibim.admin.front.security.custom;

import com.jabibim.admin.dto.StudentUserVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private JwtUserDetailService jwtUserDetailService;

  public JwtTokenProvider(JwtUserDetailService jwtUserDetailService , @Value("${jwt.secret.access}") String accessSecretKey, @Value("${jwt.secret.refresh}") String refreshSecretKey ,@Value("${jwt.expiration.access}") long AccessValidityInMilliseconds, @Value("${jwt.expiration.refresh}") long RefreshValidityInMilliseconds) {
    this.jwtUserDetailService = jwtUserDetailService;
    this.accessSecretKey = accessSecretKey;
    this.refreshSecretKey = refreshSecretKey;
    this.accessValidityInMilliseconds = AccessValidityInMilliseconds;
    this.refreshValidityInMilliseconds = RefreshValidityInMilliseconds;
  }


  // 액세스 토큰 생성
  public String createToken(StudentUserVO user, List<String> roles) {

    // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    return Jwts.builder()
        .subject(user.getStudentEmail())
        .issuedAt(Date.from(Instant.now())) // 현재 시간
        .expiration(Date.from(Instant.now().plusMillis(accessValidityInMilliseconds))) // 현재 시간 + 5분
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
        .signWith(getAccessSigningKey())
        .compact();
  }

  // 리프레쉬 토큰 생성
  public String createRefreshToken(StudentUserVO user) {
    return Jwts.builder()
        .subject(user.getStudentEmail())
        .issuedAt(Date.from(Instant.now())) // 현재 시간
        .expiration(Date.from(Instant.now().plusMillis(refreshValidityInMilliseconds))) // 현재 시간 + 1시간
        .claim("academyId", user.getAcademyId())
        .signWith(getRefreshSigningKey())
        .compact();
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

  // 액세스 토큰 검증
  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(getAccessSigningKey())
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
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

  // 액세스 토큰 기반으로 시큐리티 객체 생성해서 반환
  // 매 요청마다 검증하여 SecurityContextHolder 에 넘긴다.
  // 요청 처리 후 컨텍스트는 자동으로 정리된다.
  public Authentication getAuthentication(String token) {
    Claims claims = getClaims(token);

    // 권한 정보(roles) 추출하여 User 객체에 주입
    @SuppressWarnings("unchecked")
    Collection<? extends GrantedAuthority> authorities = ((List<String>) claims.get("roles")).stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());

    // UserDetails 객체 만들어서 SecurityContextHolder 에 넘긴다.
    // studentEmail, null, 권한(roles), StudentUserVO 담아서
    // 필요한 정보를 뽑아 사용 가능하다. => AccoutDto 와 사용법 같음
    // JwtCustomUserDetails userDetails = (JwtCustomUserDetails) auth.getPrincipal();
    // StudentUserVO user = userDetails.getUser();
    UserDetails principal = jwtUserDetailService.loadUserByUsername(claims.getSubject());

    return new UsernamePasswordAuthenticationToken(principal, token, authorities);
  }

  // 리프레시 토큰 검증
  public boolean validateRefreshToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(getRefreshSigningKey())
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  // 리프레시 토큰으로 액세스 토큰 재발급
  public String recreateAccessToken(String refreshToken) {
    Claims refreshClaims = getRefreshClaims(refreshToken);
    String username = refreshClaims.getSubject();
    String academyId = (String) refreshClaims.get("academyId");

    // 리프레시 토큰에서 사용자 정보 추출
    // DB에서 학생 정보 검색하여 액세스 토큰 생성에 사용
    JwtCustomUserDetails details = (JwtCustomUserDetails) jwtUserDetailService.loadUserByUsername(username + "," + academyId);

    // 권한 정보 추출
    @SuppressWarnings("unchecked")
    List<String> roles = details.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

    StudentUserVO user = details.getUser();

    // 액세스 토큰 생성 및 반환
    return createToken(user, roles);
  }



}
