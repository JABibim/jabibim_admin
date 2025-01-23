package com.jabibim.admin.front.api_receive.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jabibim.admin.dto.CartItemVO;
import com.jabibim.admin.front.security.custom.JwtTokenProvider;
import com.jabibim.admin.service.CartService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

  private final JwtTokenProvider tokenProvider;
  private final CartService cartService;
  private final Logger logger = LoggerFactory.getLogger(CartController.class);

  @GetMapping("/items")
  public ResponseEntity<Map<String, Object>> getCartList(HttpServletRequest request) {
    logger.info("=== 장바구니 목록 조회 요청 시작 ===");

    try {
      // 토큰 검증
      String bearerToken = request.getHeader("Authorization");
      logger.debug("Authorization header present: {}", bearerToken != null);

      if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
        logger.error("Invalid token format or missing token");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of(
                "error", "유효하지 않은 인증 토큰입니다."));
      }

      // 토큰에서 정보 추출
      String token = bearerToken.substring(7);
      Claims claims = tokenProvider.getClaims(token);
      String studentId = claims.get("studentId", String.class);
      String academyId = claims.get("academyId", String.class);

      if (studentId != null && academyId != null) {
        logger.info("Token claims extracted successfully studentId: {}, academyId: {}", studentId, academyId);
      }

      // 장바구니 조회
      List<CartItemVO> cartItems = cartService.getCartList(studentId, academyId);

      if (cartItems != null) {
        logger.debug("Retrieved cart items count: {}", cartItems.size());
      }

      int totalPrice = cartItems.stream()
          .mapToInt(CartItemVO::getCoursePrice)
          .sum();
      int totalCount = cartItems.size();

      

      // Axios 형식에 맞게 응답 데이터 구성
      Map<String, Object> response = new HashMap<>();
      response.put("items", cartItems);
      response.put("totalPrice", totalPrice);
      response.put("totalCount", totalCount);
      response.put("message", "장바구니 목록을 성공적으로 조회했습니다.");

      logger.info("=== 장바구니 목록 조회 요청 완료 ===");
      return ResponseEntity.ok(response);

    } catch (Exception e) {
      logger.error("장바구니 목록 조회 실패", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("error", e.getMessage()));
    }
  }

  @PostMapping("/add")
  public ResponseEntity<Map<String, Object>> addCartItem(
      @RequestBody CartItemVO cartItem,
      HttpServletRequest request) {
    logger.info("=== 장바구니 상품 추가 요청 시작 ===");

    try {
      String bearerToken = request.getHeader("Authorization");
      if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of(
                "error", "유효하지 않은 인증 토큰입니다."));
      }

      String token = bearerToken.substring(7);
      Claims claims = tokenProvider.getClaims(token);
      String studentId = claims.get("studentId", String.class);
      cartItem.setStudentId(studentId);

      // 장바구니 추가 로직
      cartService.addCartItem(cartItem);

      return ResponseEntity.ok(Map.of(
          "data", null,
          "message", "장바구니에 상품이 추가되었습니다."));

    } catch (Exception e) {
      logger.error("장바구니 상품 추가 실패", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("error", e.getMessage()));
    }
  }
}
