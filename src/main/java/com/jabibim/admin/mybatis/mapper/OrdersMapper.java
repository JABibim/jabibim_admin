package com.jabibim.admin.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jabibim.admin.domain.Orders;

@Mapper
public interface OrdersMapper {
  List<Orders> getOrderByPaymentId(String paymentId, String academyId);

  void insertOrders(Orders orders);

  void updateOrderStatus(Orders orders);
}
