package com.jabibim.admin.front.api_receive.controller.webhook;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@EnableRetry
public class WebhookConfig {

  @Bean
  public RetryTemplate portoneRetryTemplate() {
    RetryTemplate template = new RetryTemplate();

    // 재시도 간격 조정 (5초 → 15초 → 45초 → 135초 → 405초)
    ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
    backOffPolicy.setInitialInterval(5000); // 5초
    backOffPolicy.setMultiplier(3.0); // 3.0
    backOffPolicy.setMaxInterval(60000); // 60초

    // 재시도 가능 예외 지정
    Map<Class<? extends Throwable>, Boolean> retryMap = new HashMap<>();
    retryMap.put(WebhookProcessingException.class, true);
    retryMap.put(DataAccessException.class, true);

    SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(5, retryMap);

    template.setRetryPolicy(retryPolicy);
    template.setBackOffPolicy(backOffPolicy);

    return template;
  }

}
