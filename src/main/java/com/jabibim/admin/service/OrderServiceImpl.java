package com.jabibim.admin.service;

import com.jabibim.admin.dto.OrderApiVO;
import com.jabibim.admin.mybatis.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderMapper dao;
  private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

  @Override
  public Optional<OrderApiVO> getOrderByPaymentId(String paymentId) {
    return dao.getOrderByPaymentId(paymentId);
  }
}
