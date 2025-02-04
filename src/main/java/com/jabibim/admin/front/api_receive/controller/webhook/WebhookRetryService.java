package com.jabibim.admin.front.api_receive.controller.webhook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jabibim.admin.domain.Orders;
import com.jabibim.admin.domain.Payment;
import com.jabibim.admin.front.api_send.httpinterface.PortOneComponent;
import com.jabibim.admin.service.OrdersService;
import com.jabibim.admin.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class WebhookRetryService {

  private final Logger logger = LoggerFactory.getLogger(WebhookRetryService.class);

  private final PaymentService paymentService;
  private final OrdersService ordersService;
  private final PortOneComponent portOneService;

  @Retryable(retryFor = { WebhookProcessingException.class,
      DataAccessException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
  public boolean processWebhook(Map<String, Object> body) throws WebhookProcessingException {
    logger.info("웹훅 처리 시작");

    String type = (String) body.get("type");

    if (!type.equals("Transaction.Paid")) {
      logger.info("결제 완료 웹훅 처리 중단 타입이 다름");
      throw new WebhookProcessingException("결제 완료 웹훅 처리 중단 타입이 다름");
    }

    Map<String, Object> data = (Map<String, Object>) body.get("data");

    if (!data.containsKey("paymentId")) {
      logger.info("결제 완료 웹훅 처리 중단 결제 아이디 없음");
      throw new WebhookProcessingException("결제 완료 웹훅 처리 중단 결제 아이디 없음");
    }

    String paymentId = (String) data.get("paymentId");

    List<Payment> payments = paymentService.getPaymentByPaymentId(paymentId);

    if (payments.isEmpty()) {
      logger.info("결제 완료 웹훅 처리 중단 결제 이력 없음");
      throw new WebhookProcessingException("결제 완료 웹훅 처리 중단 결제 이력 없음");
    }

    String academyId = payments.get(0).getAcademyId();

    List<Orders> orders = ordersService.getOrderByPaymentId(paymentId, academyId);

    if (orders.isEmpty()) {
      logger.info("결제 완료 웹훅 처리 중단 주문 이력 없음");
      throw new WebhookProcessingException("결제 완료 웹훅 처리 중단 주문 이력 없음");
    }

    HashMap<String, Object> result = portOneService.getPaymentById(paymentId);

    logger.info(result.toString());

    String status = (String) result.get("status");

    if (!status.equals("PAID")) {
      logger.info("결제 완료 웹훅 처리 중단 결제 상태가 PAID가 아님");
      throw new WebhookProcessingException("결제 완료 웹훅 처리 중단 결제 상태가 PAID가 아님");
    }

    int totalPaymentAmount = 0;
    for (Payment payment : payments) {
      totalPaymentAmount += payment.getPaymentAmount();
    }

    int totalOrderAmount = 0;
    for (Orders order : orders) {
      totalOrderAmount += order.getTotalPrice();
    }

    HashMap<String, Integer> amount = (HashMap<String, Integer>) result.get("amount");

    if (totalPaymentAmount != totalOrderAmount || amount.get("total") != totalOrderAmount) {
      logger.info("결제 완료 웹훅 처리 중단 결제 금액과 주문 금액이 다름");
      throw new WebhookProcessingException("결제 완료 웹훅 처리 중단 결제 금액과 주문 금액이 다름");
    }

    paymentService.updatePaymentStatus(payments, "PAID");

    ordersService.updateOrderStatus(orders, 1);

    logger.info("결제 완료 웹훅 처리 완료");

    logger.info("웹훅 처리 완료");
    return true;
  }

  @Recover
  public boolean recoverProcess(WebhookProcessingException e) {
    logger.error("최종 실패 사유: {}", e.getMessage());
    return false;
  }
}