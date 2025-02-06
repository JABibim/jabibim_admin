package com.jabibim.admin.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jabibim.admin.domain.Cart;
import com.jabibim.admin.dto.CartItemVO;

@Mapper
public interface CartMapper {

  List<CartItemVO> getCartList(String studentId, String academyId);

  void addCartItem(Cart cart);

  void deleteCartItem(String cartId, String studentId, String academyId);
}
