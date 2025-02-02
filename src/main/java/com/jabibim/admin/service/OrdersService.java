package com.jabibim.admin.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.security.core.Authentication;

import com.jabibim.admin.domain.Orders;
import com.jabibim.admin.dto.CartItemVO;
import com.jabibim.admin.dto.OrdersDetailVO;
import com.jabibim.admin.dto.OrdersListVO;

public interface OrdersService {

  public List<Orders> getOrderByPaymentId(String paymentId, String academyId);

  public String addOrder(List<CartItemVO> carts);

  public void updateOrderStatus(List<Orders> orders, int status);

  public int getOrdersListCount(HashMap<String, String> searchMap, Authentication auth);

  public List<OrdersListVO> getOrdersList(int page, int limit, HashMap<String, String> searchMap, Authentication auth);

  public OrdersDetailVO getOrdersDetail(String ordersId, Authentication auth);
}
