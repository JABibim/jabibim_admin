package com.jabibim.admin.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jabibim.admin.domain.Orders;
import com.jabibim.admin.dto.CartItemVO;
import com.jabibim.admin.func.UUIDGenerator;
import com.jabibim.admin.mybatis.mapper.OrdersMapper;

@Service
public class OrdersServiceImpl implements OrdersService {

  private final OrdersMapper dao;
  private final Logger logger = LoggerFactory.getLogger(OrdersServiceImpl.class);

  public OrdersServiceImpl(OrdersMapper dao) {
    this.dao = dao;
  }

  @Override
  public List<Orders> getOrderByPaymentId(String paymentId, String academyId) {
    return dao.getOrderByPaymentId(paymentId, academyId);
  }

  @Override
  public String addOrder(List<CartItemVO> carts) {

    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    String orderNumber = date + "-" + UUIDGenerator.getUUID().replaceAll("-", "");
    String paymentId = UUIDGenerator.getUUID();

    for (CartItemVO cart : carts) {
      Orders orders = new Orders();
      orders.setOrdersId(UUIDGenerator.getUUID());
      orders.setOrderNumber(orderNumber);
      orders.setTotalPrice(cart.getCoursePrice());
      orders.setOrderStatus(0);
      orders.setStudentId(cart.getStudentId());
      orders.setCourseId(cart.getCourseId());
      orders.setAcademyId(cart.getAcademyId());
      orders.setPaymentId(paymentId);

      dao.insertOrders(orders);
    }

    logger.info("=== 주문 추가 완료 ===");
    return paymentId;
  }

  @Override
  public void updateOrderStatus(List<Orders> orders, int status) {
    for (Orders order : orders) {
      order.setOrderStatus(status);
      dao.updateOrderStatus(order);
    }
  }
}
