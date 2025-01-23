package com.jabibim.admin.service;

import com.jabibim.admin.dto.OrderApiVO;

import java.util.Optional;

public interface OrderService {

  public Optional<OrderApiVO> getOrderByPaymentId(String paymentId);
}
