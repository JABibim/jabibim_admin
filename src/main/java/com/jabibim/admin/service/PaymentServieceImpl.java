package com.jabibim.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jabibim.admin.domain.Orders;
import com.jabibim.admin.domain.Payment;
import com.jabibim.admin.mybatis.mapper.PaymentMapper;

@Service
public class PaymentServieceImpl implements PaymentService {

  private final PaymentMapper dao;

  public PaymentServieceImpl(PaymentMapper dao) {
    this.dao = dao;
  }

  @Override
  public void insertPayment(List<Orders> orders, String method, String paidAt, String receiptUrl) {

    for (Orders order : orders) {
      Payment payment = new Payment();
      payment.setPaymentId(order.getPaymentId());
      payment.setPaymentAmount(order.getTotalPrice());
      payment.setPaymentMethod(method);
      payment.setPaymentStatus("PENDING");
      payment.setPaidAt(paidAt);
      payment.setReceiptUrl(receiptUrl);
      payment.setStudentId(order.getStudentId());
      payment.setOrdersId(order.getOrdersId());
      payment.setCourseId(order.getCourseId());
      payment.setAcademyId(order.getAcademyId());

      dao.insertPayment(payment);
    }

  }

  @Override
  public List<Payment> getPaymentByPaymentId(String paymentId) {
    return dao.getPaymentByPaymentId(paymentId);
  }

  @Override
  public void updatePaymentStatus(List<Payment> payments, String status) {
    for (Payment payment : payments) {
      payment.setPaymentStatus(status);
      dao.updatePaymentStatus(payment);
    }
  }
}
