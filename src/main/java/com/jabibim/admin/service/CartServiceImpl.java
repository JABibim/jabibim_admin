package com.jabibim.admin.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jabibim.admin.domain.Cart;
import com.jabibim.admin.dto.CartItemVO;
import com.jabibim.admin.dto.CourseDetailVO;
import com.jabibim.admin.func.UUIDGenerator;
import com.jabibim.admin.mybatis.mapper.CartMapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

  private final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

  @NonNull
  private CartMapper dao;

  @Override
  public List<CartItemVO> getCartList(String studentId, String academyId) {
    logger.info("=== 장바구니 목록 조회 서비스 시작 ===");

    try {
      logger.debug("Querying cart items for academy");
      List<CartItemVO> cartItems = dao.getCartList(studentId, academyId);

      if (cartItems == null) {
        logger.warn("No cart items found");
        return List.of();
      }

      logger.debug("Retrieved {} cart items", cartItems.size());
      logger.info("=== 장바구니 목록 조회 서비스 완료 ===");

      return cartItems;

    } catch (Exception e) {
      logger.error("장바구니 목록 조회 중 오류 발생: {}", e.getMessage());
      throw new RuntimeException("장바구니 목록을 가져오는데 실패했습니다.", e);
    }
  }

  @Override
  public void addCartItem(String courseId, String studentId, String academyId) {
    logger.info("=== 장바구니 상품 추가 서비스 시작 ===");
    String cartId = UUIDGenerator.getUUID();

    Cart cart = new Cart();
    cart.setCartId(cartId);
    cart.setStudentId(studentId);
    cart.setAcademyId(academyId);
    cart.setCourseId(courseId);

    dao.addCartItem(cart);
  }

  @Override
  public void deleteCartItem(String cartId, String studentId, String academyId) {
    dao.deleteCartItem(cartId, studentId, academyId);
  }
}
