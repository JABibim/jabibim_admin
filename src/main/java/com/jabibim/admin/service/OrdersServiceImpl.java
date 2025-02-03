package com.jabibim.admin.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.jabibim.admin.domain.Orders;
import com.jabibim.admin.dto.CartItemVO;
import com.jabibim.admin.dto.OrdersDetailVO;
import com.jabibim.admin.dto.OrdersListVO;
import com.jabibim.admin.func.UUIDGenerator;
import com.jabibim.admin.mybatis.mapper.OrdersMapper;
import com.jabibim.admin.security.dto.AccountDto;

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
      orders.setOrdersNumber(orderNumber);
      orders.setTotalPrice(cart.getCoursePrice());
      orders.setOrdersStatus(0);
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
      order.setOrdersStatus(status);
      dao.updateOrderStatus(order);
    }
  }

  @Override
  public int getOrdersListCount(HashMap<String, String> searchMap, Authentication auth) {
    AccountDto account = (AccountDto) auth.getPrincipal();
    String academyId = account.getAcademyId();
    HashMap<String, Object> hm = getOrderDate(searchMap);
    hm.put("academyId", academyId);
    return dao.getOrdersListCount(hm);
  }

  @Override
  public List<OrdersListVO> getOrdersList(int page, int limit, HashMap<String, String> searchMap, Authentication auth) {
    AccountDto account = (AccountDto) auth.getPrincipal();
    String academyId = account.getAcademyId();
    HashMap<String, Object> hm = getOrderDate(searchMap);
    hm.put("academyId", academyId);
    hm.put("offset", (page - 1) * limit);
    hm.put("limit", limit);

    return dao.getOrdersList(hm);
  }

  @Override
  public OrdersDetailVO getOrdersDetail(String ordersId, Authentication auth) {
    AccountDto account = (AccountDto) auth.getPrincipal();
    String academyId = account.getAcademyId();
    return dao.getOrdersDetail(ordersId, academyId);
  }

  private static HashMap<String, Object> getOrderDate(HashMap<String, String> searchMap) {
    HashMap<String, Object> orderDateMap = new HashMap<>();

    if (!searchMap.get("orderDate1").equals("")) {
      orderDateMap.put("orderDate1", searchMap.get("orderDate1"));
    }
    if (!searchMap.get("orderDate2").equals("")) {
      orderDateMap.put("orderDate2", searchMap.get("orderDate2"));
    }

    if (!searchMap.get("paymentDate1").equals("")) {
      orderDateMap.put("paymentDate1", searchMap.get("paymentDate1"));
    }

    if (!searchMap.get("paymentDate2").equals("")) {
      orderDateMap.put("paymentDate2", searchMap.get("paymentDate2"));
    }

    if (!searchMap.get("paymentStatus").equals("all")) {
      orderDateMap.put("paymentStatus", searchMap.get("paymentStatus"));
    }

    if (!searchMap.get("paymentMethod").equals("all")) {
      orderDateMap.put("paymentMethod", searchMap.get("paymentMethod"));
    }

    if (!searchMap.get("searchWord").equals("")) {
      orderDateMap.put("searchField", searchMap.get("searchField").split(""));
      orderDateMap.put("searchWord", "%" + searchMap.get("searchWord") + "%");
    }

    return orderDateMap;
  }
}
