package com.jabibim.admin.service;

import java.util.List;

import com.jabibim.admin.domain.Orders;
import com.jabibim.admin.dto.CartItemVO;

public interface OrdersService {

  public List<Orders> getOrderByPaymentId(String paymentId, String academyId);

  public String addOrder(List<CartItemVO> carts);

  public void updateOrderStatus(List<Orders> orders, int status);
}
