package com.jabibim.admin.front.api_send;

import com.jabibim.admin.front.api_send.httpinterface.PortOneComponent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;


@Configuration
@PropertySource("/properties/payments.properties")
public class RestClientConfig {
  // 스프링 부트에서 외부 api 에 요청을 보낼 때 사용하는 클래스.
  // 각자 필요한 인터페이스를 builder 메서드를 사용해서 필요한 시크릿 키 및 요청을 보낼 type 을 설정한다.

  @Value("${portOne.api.secret_key}")
  private String portOneSecret;

  @Bean
  public PortOneComponent portOneService() {
     RestClient restClient = RestClient.builder().baseUrl("https://api.portone.io").defaultHeaders(httpHeaders -> {
       httpHeaders.set("Authorization", "PortOne " + portOneSecret);
       httpHeaders.set("Content-Type", "application/json");
     }).build();
     RestClientAdapter adapter = RestClientAdapter.create(restClient);
     HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

    return factory.createClient(PortOneComponent.class);
  }
}
