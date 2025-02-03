package com.jabibim.admin.service;

import java.util.List;

import com.jabibim.admin.domain.Orders;
import com.jabibim.admin.domain.Payment;

public interface PaymentService {

  public void insertPayment(List<Orders> orders, String method, String paidAt, String receiptUrl);

  public List<Payment> getPaymentByPaymentId(String paymentId);

  public void updatePaymentStatus(List<Payment> payments, String status);

}
