package com.jabibim.admin.security.customfilter;

import com.jabibim.admin.security.provider.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(
        HttpServletRequest request
      , HttpServletResponse response
      , FilterChain filterChain) throws ServletException, IOException {

    String token = getJwtFromRequest(request);

    // jwt 토큰을 가져와서 jwtTokenProvider 에서 유효성 검사 및 
    // 통과 시 SecurityContextHolder 에 auth 저장 
    if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
      Authentication auth = jwtTokenProvider.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(auth);
    }

    filterChain.doFilter(request, response);
  }

  // Request 헤더로부터 jwt 토큰 가져오기
  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(6);
    }
    return null;
  }
}
