package com.jabibim.admin.front.api_receive.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.jabibim.admin.domain.Orders;
import com.jabibim.admin.dto.CartItemVO;
import com.jabibim.admin.dto.OrderApiVO;
import com.jabibim.admin.front.api_send.httpinterface.PortOneComponent;
import com.jabibim.admin.front.dto.PaymentVO;
import com.jabibim.admin.service.CartService;
import com.jabibim.admin.service.OrdersService;
import com.jabibim.admin.service.PaymentService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

// 결제 관련 api 요청 처리하는 컨트롤러
@Controller
@RequiredArgsConstructor
public class PaymentsController {

  private final Logger logger = LoggerFactory.getLogger(PaymentsController.class);
  @NonNull
  private OrdersService orderService;
  @NonNull
  private PortOneComponent portOneService;
  @NonNull
  private CartService cartService;
  @NonNull
  private PaymentService paymentService;

  @PostMapping(value = "/api/public/paycheck", headers = { "Content-Type=application/json" })
  @ResponseBody
  public String getOrderById(@RequestBody(required = true) PaymentVO payment, HttpServletResponse response)
      throws Exception {

    logger.info(payment.toString());

    JsonObject jsonObject = new JsonObject();

    HashMap<String, Object> result = portOneService.getPaymentById(payment.getPaymentId());

    switch ((String) result.get("status")) {
      case "PAID" -> {
        logger.info("Payment status is PAID");
        HashMap<String, Integer> amount = (HashMap<String, Integer>) result.get("amount");

        logger.info("Payment amount is " + amount.get("total"));

        List<Orders> orders = orderService.getOrderByPaymentId(payment.getPaymentId(), payment.getAcademyId());

        if (orders.size() == 0) {
          response.setContentType(MediaType.APPLICATION_JSON_VALUE);
          response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
          PrintWriter out = response.getWriter();
          out.print("{\"error\": \"주문 조회 실패\"}");
          return null;
        }

        int totalPrice = 0;
        for (Orders order : orders) {
          totalPrice += order.getTotalPrice();
        }

        if (totalPrice != amount.get("total")) {
          response.setContentType(MediaType.APPLICATION_JSON_VALUE);
          response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
          PrintWriter out = response.getWriter();
          out.print("{\"error\": \"주문 금액 불일치\"}");
          return null;
        }

        HashMap<String, String> paymentMethod = (HashMap<String, String>) result.get("method");

        paymentService.insertPayment(orders, paymentMethod.get("type"), (String) result.get("paidAt"),
            (String) result.get("receiptUrl"));

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.print("{\"ok\": true}");

      }

      case "FAILED" -> {
        logger.info("Payment status is FAILED");
      }

      case "CANCELLED" -> {
        logger.info("Payment status is CANCELLED");
      }

      case "PAY_PENDING" -> {
        logger.info("Payment status is PAY_PENDING");
      }

      case "READY" -> {
        logger.info("Payment status is READY");
      }
    }

    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    out.print(jsonObject);

    return null;
  }

  // 주문 추가
  @PostMapping(value = "/api/public/addOrder", headers = { "Content-Type=application/json" })
  public String updateOrderStatus(@RequestBody(required = true) OrderApiVO order, HttpServletResponse response)
      throws IOException {
    logger.info("=== 주문 추가 요청 시작 ===");

    List<CartItemVO> carts = cartService.getCartList(order.getStudentId(), order.getAcademyId());

    String paymentId = orderService.addOrder(carts);

    if (paymentId == null) {
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      PrintWriter out = response.getWriter();
      out.print("{\"error\": \"주문 추가 실패\"}");
      return null;
    }

    logger.info("=== 주문 추가 요청 완료 ===");

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_OK);

    PrintWriter out = response.getWriter();
    out.print("{\"paymentId\": \"" + paymentId + "\"}");

    return null;
  }

}
