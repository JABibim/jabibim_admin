package com.jabibim.admin.front.api_receive.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jabibim.admin.dto.CartItemVO;
import com.jabibim.admin.dto.CourseDetailVO;
import com.jabibim.admin.dto.StudentUserVO;
import com.jabibim.admin.front.security.custom.JwtCustomUserDetails;
import com.jabibim.admin.service.CartService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;
  private final Logger logger = LoggerFactory.getLogger(CartController.class);

  @GetMapping("/list")
  public ResponseEntity<?> getCartList(HttpServletRequest request) {
    try {
      logger.info("=== 장바구니 목록 조회 요청 시작 ===");

      // SecurityContext에서 인증 정보 가져오기
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication == null || !authentication.isAuthenticated()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 사용자입니다.");
      }

      JwtCustomUserDetails userDetails = (JwtCustomUserDetails) authentication.getPrincipal();
      StudentUserVO student = userDetails.getUser();

      // 장바구니 조회
      List<CartItemVO> cartItems = cartService.getCartList(student.getStudentId(), student.getAcademyId());

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
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("장바구니 목록 조회 실패");
    }
  }

  @PostMapping("/add")
  public ResponseEntity<Map<String, Object>> addCartItem(
      @RequestBody(required = true) Map<String, Object> input,
      Authentication auth) {
    logger.info("=== 장바구니 상품 추가 요청 시작 ===");

    if (auth == null || !auth.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("error", "인증되지 않은 사용자입니다."));
    }

    JwtCustomUserDetails userDetails = (JwtCustomUserDetails) auth.getPrincipal();
    StudentUserVO student = userDetails.getUser();

    String id = (String) input.get("courseId");

    // 장바구니 추가 로직
    cartService.addCartItem(id, student.getStudentId(), student.getAcademyId());

    return ResponseEntity.ok(Map.of(
        "message", "장바구니에 상품이 추가되었습니다."));
  }

  @DeleteMapping("/{cartId}")
  public ResponseEntity<Map<String, Object>> deleteCartItem(
      @PathVariable String cartId,
      HttpServletRequest request) {
    logger.info("=== 장바구니 상품 삭제 요청 시작 ===");

    // SecurityContext에서 인증 정보 가져오기
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("error", "인증되지 않은 사용자입니다."));
    }

    JwtCustomUserDetails userDetails = (JwtCustomUserDetails) authentication.getPrincipal();
    StudentUserVO student = userDetails.getUser();

    cartService.deleteCartItem(cartId, student.getStudentId(), student.getAcademyId());

    HashMap<String, Object> response = new HashMap<>();
    response.put("data", null);
    response.put("message", "장바구니 상품이 삭제되었습니다.");

    return ResponseEntity.ok(response);

  }
}
