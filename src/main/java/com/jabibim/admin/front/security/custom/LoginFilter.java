package com.jabibim.admin.front.security.custom;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jabibim.admin.dto.StudentUserVO;
import com.jabibim.admin.front.dto.LoginRequest;
import com.jabibim.admin.front.properties.AcademyProperties;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
  // 커스텀 UsernamePassword 필터

  private final JwtTokenProvider jwtTokenProvider;
  private String secretKey;
  private String academyId;

  public LoginFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
      AcademyProperties academyProperties) {
    super.setAuthenticationManager(authenticationManager);
    this.jwtTokenProvider = jwtTokenProvider;
    this.secretKey = academyProperties.getSecretKey();
    this.academyId = academyProperties.getAcademyId();
    setFilterProcessesUrl("/api/auth/login");
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {
    logger.info("LoginFilter.attemptAuthentication called");
    logger.info("Request URI: " + request.getRequestURI());
    logger.info("Request Method: " + request.getMethod());
    logger.info("Content-Type: " + request.getContentType());

    try {
      // Request Body를 읽어서 LoginRequest 객체로 변환
      ObjectMapper objectMapper = new ObjectMapper();
      LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

      // 디버깅을 위한 로그
      logger.info("Received login request - email" + loginRequest.getEmail());

      String target = getKeyRequest(request);
      String id = getAcademyId(target);

      // 클라이언트 요청에서 username, password 추출 및 academyId 같이 보내기
      String username = loginRequest.getEmail() + "," + id; // principal
      String password = loginRequest.getPassword(); // credentials

      // spring security 에서 검증하기 위해 token 에 담아야 한다.
      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

      return super.getAuthenticationManager().authenticate(authToken);

    } catch (Exception e) {
      logger.error("Failed to parse authentication request", e);
      throw new AuthenticationServiceException("Failed to parse authentication request", e);
    }

  }

  // 로그인 성공 시 jwt 발급 액세스 + 리프레시
  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication auth) throws IOException {

    // 1. 인증된 사용자 정보 추출
    JwtCustomUserDetails userDetails = (JwtCustomUserDetails) auth.getPrincipal();
    StudentUserVO user = userDetails.getUser();

    // 2. 권한 정보 추출
    List<String> roles = auth.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    // 3. JWT 토큰 생성
    String accessToken = jwtTokenProvider.createToken(user, roles);
    String refreshToken = jwtTokenProvider.createRefreshToken(user);

    Cookie cookie = new Cookie("refresh", refreshToken);
    cookie.setHttpOnly(true); // 자바스크립트에서 쿠키 변조 못하게 방지
    cookie.setMaxAge(60 * 60 * 24 * 7); // 쿠키 유지 7일
    // cookie.setSecure(true); // 쿠키 보안 설정 https 프로토콜 사용할 때 활성화
    cookie.setPath("/"); // 모든 경로에서 사용
    response.addCookie(cookie);

    // 4. 응답 생성
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.OK.value()); // 200 번대 성공 코드 반환
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    // 5. 응답 바디 작성
    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("accessToken", "Bearer " + accessToken);

    new ObjectMapper().writeValue(response.getWriter(), responseBody);
    /*
     * 인증 관련 상태 코드
     * => Enum 으로 명시하는게 표준이라 함.
     * response.setStatus(HttpStatus.OK.value()); // 200
     * response.setStatus(HttpStatus.CREATED.value()); // 201 생성
     * response.setStatus(HttpStatus.NO_CONTENT.value()); // 204 삭제
     * response.setStatus(HttpStatus.NOT_FOUND.value()); // 404 찾을 수 없음
     * response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()); // 500 서버 오류
     * response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401 인증 실패
     * response.setStatus(HttpStatus.FORBIDDEN.value()); // 403 권한 없음
     * response.setStatus(HttpStatus.BAD_REQUEST.value()); // 400 잘못된 요청
     */
  }

  // 로그인 실패 시 처리
  @Override
  protected void unsuccessfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException failed) throws IOException {

    response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401 번 인증 실패 코드 반환
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());

    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("error", "Authentication failed");
    errorResponse.put("message", failed.getMessage());
  }

  // Request 헤더로부터 학원 id를 알기 위한 식별키 가져오기
  private String getKeyRequest(HttpServletRequest request) {
    String targetKey = request.getHeader("Bibim");
    if (StringUtils.hasText(targetKey) && targetKey.startsWith("Bibim")) {
      return targetKey.substring(6);
    }
    return "";
  }

  private String getAcademyId(String targetKey) {
    if (targetKey.isEmpty()) {
      return "";
    }

    String[] secretKeys = secretKey.split(",");
    String[] academyIds = academyId.split(",");

    for (int i = 0; i < secretKeys.length; i++) {
      if (secretKeys[i].equals(targetKey)) {
        return academyIds[i];
      }
    }

    return "";
  }
}
