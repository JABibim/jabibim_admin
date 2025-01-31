package com.jabibim.admin.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jabibim.admin.domain.Payment;

@Mapper
public interface PaymentMapper {

  public void insertPayment(Payment payment);

  public List<Payment> getPaymentByPaymentId(String paymentId);

  public void updatePaymentStatus(Payment payment);
}
