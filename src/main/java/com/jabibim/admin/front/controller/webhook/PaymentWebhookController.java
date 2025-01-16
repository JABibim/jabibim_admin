package com.jabibim.admin.front.controller.webhook;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Set;

import static com.jabibim.admin.func.IpInfo.getClientIp;

@Controller
@RequestMapping("/webhook/payments")
@PropertySource("/properties/payments.properties")
public class PaymentWebhookController {

  @Value("${portOne.webhook.secret_key}")
  private String secretKey;

  private final Logger logger = LoggerFactory.getLogger(PaymentWebhookController.class);

  @Value("${portOne.webhook.ip_address}")
  private String portOneIpFilter;

  @PostMapping("/receive")
  @ResponseBody
  public String receive(@RequestParam(required=false) String type
                        , @RequestParam(required=false) String timestamp
                        , @RequestParam(required=false) HashMap<String, String> data
                        , HttpServletRequest request
                        , HttpServletResponse response
  ) throws Exception {

    boolean ipCheck = checkIpAddress(logger, request, portOneIpFilter);

    if (!ipCheck) {
      logger.error("웹훅 요청 검증 중단");
      return null;
    }

    logger.info(type);
    logger.info(timestamp);
    Set<String> entry =  data.keySet();
    for (String key : entry) {
      logger.info(key + " : " + data.get(key));
    }

    // 웹훅에 대한 처리하고 회신
    JsonObject json = new JsonObject();
    json.addProperty("message", "success");
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();

    out.print(json);

    return null;
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


}
