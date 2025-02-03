package com.jabibim.admin.front.api_receive.controller.webhook;

import static com.jabibim.admin.func.IpInfo.getClientIp;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.jabibim.admin.front.api_send.httpinterface.PortOneComponent;
import com.jabibim.admin.service.OrdersService;
import com.jabibim.admin.service.PaymentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

// 결제관련 웹 훅을 받는 엔드포인트
@RestController
@RequestMapping("/api/webhook/payments")
@PropertySource("/properties/payments.properties")
@RequiredArgsConstructor
public class PaymentWebhookController {

  private final Logger logger = LoggerFactory.getLogger(PaymentWebhookController.class);

  @Value("${portOne.webhook.ip_address}")
  private String portOneIpFilter;

  @Value("${portOne.webhook.secret_key}")
  private String secretKey;

  @NonNull
  private PaymentService paymentService;
  @NonNull
  private OrdersService orderService;

  @NonNull
  private PortOneComponent portOneService;

  @NonNull
  private WebhookRetryService webhookRetryService;

  @PostMapping("/receive")
  public String handleWebhook(
      @RequestHeader("webhook-signature") String signature,
      @RequestHeader("webhook-timestamp") String timestamp,
      @RequestHeader("webhook-id") String id,
      @RequestBody String payload, HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    try {

      TimeUnit.SECONDS.sleep(10);

      boolean ipCheck = checkIpAddress(logger, request, portOneIpFilter);

      if (!ipCheck) {
        logger.error("웹훅 요청 검증 중단 확인되지 않은 포트원 webhook 요청 IP 주소입니다");
        throw new WebhookProcessingException("확인되지 않은 포트원 webhook 요청 IP 주소입니다");
      }

      // if (!verifySignature(payload, signature, timestamp, id)) {
      // logger.error("웹훅 요청 검증 중단 서명 검증 실패");
      // response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
      // return "서명 검증 실패";
      // }
      //
      // if (!validateTimestamp(timestamp)) {
      // logger.error("웹훅 요청 검증 중단 타임스탬프 검증 실패");
      // response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      // return "타임스탬프 검증 실패";
      // }

      Gson gson = new Gson();

      if (payload.isEmpty()) {
        throw new WebhookProcessingException("페이로드가 비어있습니다");
      }

      Map<String, Object> body = gson.fromJson(payload, HashMap.class);

      if (webhookRetryService.processWebhook(body)) {
        response.setStatus(HttpServletResponse.SC_OK);
        return "웹훅 처리 완료";
      } else {
        throw new WebhookProcessingException("웹훅 처리 실패");
      }

    } catch (Exception e) {
      logger.error("웹훅 처리 중 오류 발생", e);
      throw new WebhookProcessingException("웹훅 처리 중 오류 발생");
    }
  }

  private static boolean checkIpAddress(Logger logger, HttpServletRequest request, String portOneIpFilter) {

    // func 패키지에 만든 IpInfo 클래스의 스태틱 메서드. 요청에 대한 IP 정보를 가져온다.
    String requestIp = getClientIp(request);

    logger.info("웹훅 요청 IP ====> " + requestIp);

    if (!requestIp.equals(portOneIpFilter)) {
      logger.error("확인되지 않은 포트원 webhook 요청 IP 주소입니다 ====> " + requestIp);
      return false;
    } else {
      logger.info("인증된 포트원 IP 주소 입니다 ====> " + requestIp);
      return true;
    }
  }

  private boolean verifySignature(String payload, String signature, String timestamp, String id) {
    try {
      String signedPayload = timestamp + "." + id + "." + payload;
      Mac hmac = Mac.getInstance("HmacSHA256");
      hmac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
      byte[] signatureBytes = hmac.doFinal(signedPayload.getBytes(StandardCharsets.UTF_8));
      String expectedSignature = HexFormat.of().formatHex(signatureBytes);

      return signature.equals(expectedSignature);
    } catch (Exception e) {
      logger.error("Signature verification failed", e);
      return false;
    }
  }

  private boolean validateTimestamp(String timestamp) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
    LocalDateTime timestampDateTime = LocalDateTime.parse(timestamp, formatter);
    LocalDateTime now = LocalDateTime.now();
    return timestampDateTime.isAfter(now.minusMinutes(5)) && timestampDateTime.isBefore(now.plusMinutes(5));
  }

}
