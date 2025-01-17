package com.jabibim.admin.security.customfilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jabibim.admin.dto.StudentUserVO;
import com.jabibim.admin.front.dto.LoginRequest;
import com.jabibim.admin.security.details.JwtCustomUserDetails;
import com.jabibim.admin.security.dto.AcademyProperties;
import com.jabibim.admin.security.provider.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

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
      String username = loginRequest.getEmail() + "," + id;
      String password = loginRequest.getPassword();

      // spring security 에서 검증하기 위해 token 에 담아야 한다.
      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

      return super.getAuthenticationManager().authenticate(authToken);

    } catch (Exception e) {
      logger.error("Failed to parse authentication request", e);
      throw new AuthenticationServiceException("Failed to parse authentication request", e);
    }

  }

  // 로그인 성공 시 jwt 발급
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication auth) throws IOException, ServletException {

    logger.info("LoginFilter.successfulAuthentication called" + auth);
    // JwtCustomUserDetails로 캐스팅
    JwtCustomUserDetails userDetails = (JwtCustomUserDetails) auth.getPrincipal();

    // StudentUserVO 가져오기
    StudentUserVO user = userDetails.getUser();
    List<String> authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    String token = jwtTokenProvider.createToken(user, authorities);

    response.setHeader("Authorization", "Bearer " + token);

    // JSON 응답 생성
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(
        String.format("{\"token\":\"Bearer %s\"}", token));
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException failed) throws IOException, ServletException {

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write("{\"error\":\"Authentication failed\"}");

    // 여기서 메서드를 종료하고 다음 필터로 전달하지 않음
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
