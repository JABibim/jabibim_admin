package com.jabibim.admin.func;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class SlackWebhookNotifier implements ApplicationListener<ApplicationReadyEvent> {
    @Value("${slack.webhook.url}")
    private String slackWebhookUrl;
    private final Environment env;

    public SlackWebhookNotifier(Environment env) {
        this.env = env;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (Arrays.asList(env.getActiveProfiles()).contains("dev")) {
            System.out.println("Slack 웹훅 전송: 개발 환경에서는 전송하지 않습니다.");
        } else {
            Map<String, Object> payload = new HashMap<>();
            payload.put("text", "🚀 서버가 정상적으로 기동되었습니다. 🚀");

            RestTemplate restTemplate = new RestTemplate();
            try {
                restTemplate.postForEntity(slackWebhookUrl, payload, String.class);
            } catch (Exception e) {
                System.err.println("Slack 웹훅 전송 실패: " + e.getMessage());
            }
        }
    }
}
