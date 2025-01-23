package com.jabibim.admin.service;

import com.jabibim.admin.dto.CartItemVO;

import java.util.List;

public interface CartService {

  List<CartItemVO> getCartList(String studentId, String academyId);

  void addCartItem(CartItemVO cartItem);
}
