package com.jabibim.admin.front.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jabibim.admin.dto.OrderApiVO;
import com.jabibim.admin.front.dto.PaymentVO;
import com.jabibim.admin.front.httpinterface.PortOneComponent;
import com.jabibim.admin.service.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PaymentsController {


  private final Logger logger = LoggerFactory.getLogger(PaymentsController.class);
  @NonNull
  private OrderService orderService;
  @NonNull
  private PortOneComponent portOneService;

  @PostMapping(value = "/api/public/paycheck", headers = {"Content-Type=application/json"})
  @ResponseBody
  public String getOrderById(@RequestBody(required = true) PaymentVO payment, HttpServletResponse response) throws Exception {

    logger.info(payment.toString());

    Gson gson = new Gson();

    JsonObject jsonObject = new JsonObject();

    HashMap<String, Object> result = portOneService.getPaymentById(payment.getPaymentId());

    Optional<OrderApiVO> vo = orderService.getOrderByPaymentId(payment.getPaymentId());


    switch ((String) result.get("status")) {
      case "PAID" -> {
        logger.info("Payment status is PAID");
        HashMap<String, Integer> amount = (HashMap<String, Integer>) result.get("amount");

        logger.info("Payment amount is " + amount.get("total"));
        logger.info("Paid amount is " + amount.get("paid"));

        if (amount.get("total") == vo.get().getPaymentAmount()) {
          jsonObject.addProperty("ok", true);
        }

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


}
