package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.dto.CartItemVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartMapper {

  List<CartItemVO> getCartList(String studentId, String academyId);

  void addCartItem(CartItemVO cartItem);

  void deleteCartItem(String cartId, String studentId, String academyId);
}
