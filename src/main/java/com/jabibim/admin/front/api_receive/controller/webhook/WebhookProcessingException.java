package com.jabibim.admin.front.api_receive.controller.webhook;

public class WebhookProcessingException extends RuntimeException {
  public WebhookProcessingException(String message) {
    super(message);
  }
}
