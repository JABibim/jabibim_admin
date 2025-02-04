package com.jabibim.admin.front.api_receive.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jabibim.admin.domain.Student;
import com.jabibim.admin.front.security.custom.JwtTokenProvider;
import com.jabibim.admin.service.RedisService;
import com.jabibim.admin.service.StudentService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class FrontAuthController {

  private final Logger logger = LoggerFactory.getLogger(FrontAuthController.class);
  private final StudentService studentService;
  private final RedisService redisService;
  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping("/logout")
  public ResponseEntity<?> logout(
      @RequestHeader("Authorization") String token, HttpServletResponse response) {

    logger.info("=== 로그아웃 요청 시작 ===");
    logger.info("Received token: {}", token);

    // Authorization 헤더에서 토큰 추출
    // 향후 해당 토큰의 유효시간까지 블랙리스트에 올려서 사용 불가하도록 처리
    // 레디스나 db를 사용한다.
    String accessToken = token.substring(7);
    long expiration = jwtTokenProvider.getClaims(accessToken).getExpiration().getTime();
    long remainingTime = Math.max(expiration - System.currentTimeMillis(), 0);

    if (remainingTime > 0) {
      logger.info("=== 레디스 블랙리스트 추가 시작 ===");
      redisService.addToBlackList(accessToken, remainingTime);
      logger.info("=== 레디스 블랙리스트 추가 완료 ===");
    }

    // 리프레쉬 토큰 제거
    Cookie cookie = new Cookie("refreshToken", null);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    cookie.setAttribute("SameSite", "None"); // CORS 요청에서 쿠키 전송 허용
    response.addCookie(cookie);

    logger.info("=== 로그아웃 요청 완료 ===");
    return ResponseEntity.ok()
        .body(Map.of("message", "Logout successful"));

  }

  @PostMapping("/join")
  public ResponseEntity<Map<String, Object>> join(@RequestBody(required = true) Student student) {
    logger.info("=== 회원가입 요청 시작 ===");
    logger.debug("Received student data: {}", student);

    try {
      // 필수 필드 검증
      if (student.getStudentPassword() == null || student.getStudentPassword().trim().isEmpty()) {
        logger.warn("Password is missing or empty");
        return ResponseEntity.badRequest()
            .body(Map.of(
                "status", "error",
                "message", "비밀번호는 필수 입력값입니다."));
      }

      boolean isSuccess = studentService.insertStudent(student);

      if (isSuccess) {
        logger.info("=== 회원가입 성공 ===");
        return ResponseEntity.ok()
            .body(Map.of(
                "status", "success",
                "message", "회원가입이 완료되었습니다."));
      } else {
        logger.error("Failed to insert student");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of(
                "status", "error",
                "message", "회원가입 처리 중 오류가 발생했습니다."));
      }
    } catch (Exception e) {
      logger.error("회원가입 처리 중 예외 발생", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of(
              "status", "error",
              "message", e.getMessage()));
    }
  }

  // @PostMapping("/signup")
  // public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest request) {
  // // 회원가입 처리
  // }

}
