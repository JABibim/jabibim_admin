package com.jabibim.admin.front.httpinterface;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.HashMap;

public interface PortOneComponent {

  @GetExchange("/payments/{paymentId}")
  HashMap<String, Object> getPaymentById(@PathVariable("paymentId") String paymentId);
}
