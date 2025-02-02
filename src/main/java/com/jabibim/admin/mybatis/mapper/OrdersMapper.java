package com.jabibim.admin.mybatis.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jabibim.admin.domain.Orders;
import com.jabibim.admin.dto.OrdersListVO;

@Mapper
public interface OrdersMapper {
  List<Orders> getOrderByPaymentId(String paymentId, String academyId);

  void insertOrders(Orders orders);

  void updateOrderStatus(Orders orders);

  int getOrdersListCount(HashMap<String, Object> map);

  List<OrdersListVO> getOrdersList(HashMap<String, Object> map);
}
