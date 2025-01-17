package com.jabibim.admin.front;

import com.jabibim.admin.front.httpinterface.PortOneComponent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Configuration
@PropertySource("/properties/payments.properties")
public class RestClientConfig {

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
