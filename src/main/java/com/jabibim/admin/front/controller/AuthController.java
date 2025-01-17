package com.jabibim.admin.front.controller;

import com.jabibim.admin.dto.StudentUserVO;
import com.jabibim.admin.front.dto.LoginRequest;
import com.jabibim.admin.front.dto.TokenResponse;
import com.jabibim.admin.security.provider.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final Logger logger = LoggerFactory.getLogger(AuthController.class);


//  @PostMapping("/login")
//  public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest, Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
//
//    logger.info("jwt 로그인 시작");
//
//    String Token = nu
//
//
//
//    return ResponseEntity.ok(true);
//
//  }


//  @PostMapping("/signup")
//  public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest request) {
//    // 회원가입 처리
//  }
//
//  @PostMapping("/refresh")
//  public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
//    // 토큰 갱신 처리
//  }
//
//  @PostMapping("/logout")
//  public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
//    // 로그아웃 처리
//  }
}
