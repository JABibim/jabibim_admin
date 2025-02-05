package com.jabibim.admin.service;

import java.util.List;

import com.jabibim.admin.dto.CartItemVO;

public interface CartService {

  List<CartItemVO> getCartList(String studentId, String academyId);

  void addCartItem(String courseId, String studentId, String academyId);

  void deleteCartItem(String cartId, String studentId, String academyId);
}
