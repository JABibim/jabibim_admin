package com.jabibim.admin.security.customfilter;

import com.jabibim.admin.dto.StudentUserVO;
import com.jabibim.admin.security.dto.AcademyProperties;
import com.jabibim.admin.security.provider.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private String secretKey;
  private String academyId;

  public LoginFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, AcademyProperties academyProperties) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
    this.secretKey = academyProperties.getSecretKey();
    this.academyId = academyProperties.getAcademyId();

  }

  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    String target = getKeyRequest(request);
    String academyId = getAcademyId(target);

    // 클라이언트 요청에서 username, password 추출 및 academyId 같이 보내기
    String username = obtainUsername(request) + "," + academyId;
    String password = obtainPassword(request);

    // spring security 에서 검증하기 위해 token 에 담아야 한다.
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

    return authenticationManager.authenticate(authToken);
  }

  // 로그인 성공 시 jwt 발급
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
    List<String> authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

    String token = jwtTokenProvider.createToken((StudentUserVO) auth.getPrincipal(), authorities);


    // JSON 응답 생성
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(
        String.format("{\"Authorization\":\"Bearer %s\"}", token)
    );
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
      return targetKey.substring(7);
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
