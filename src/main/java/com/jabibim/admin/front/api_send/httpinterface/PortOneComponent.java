package com.jabibim.admin.front.api_send.httpinterface;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.HashMap;

// 포트원 api v2 에 api 요청 보내는 컴포넌트 인터페이스
public interface PortOneComponent {

  @GetExchange("/payments/{paymentId}")
  HashMap<String, Object> getPaymentById(@PathVariable("paymentId") String paymentId);
}
