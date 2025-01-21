package com.jabibim.admin.front.api_receive.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final Logger logger = LoggerFactory.getLogger(AuthController.class);

   @PostMapping("/logout")
 public ResponseEntity<?> logout(
  @RequestHeader("Authorization") String token
  , HttpServletResponse response
  ) {

    // Authorization 헤더에서 토큰 추출
    // 향후 해당 토큰의 유효시간까지 블랙리스트에 올려서 사용 불가하도록 처리
    // 레디스나 db를 사용한다.
    String accessToken = token.substring(7);

    // 리프레쉬 토큰 제거
    Cookie cookie = new Cookie("refresh", null);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    response.addCookie(cookie);

    return ResponseEntity.ok()
    .body(Map.of("message", "Logout successful")); 
   
 }


//  @PostMapping("/signup")
//  public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest request) {
//    // 회원가입 처리
//  }


}
