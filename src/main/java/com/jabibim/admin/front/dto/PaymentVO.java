package com.jabibim.admin.front.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentVO {
  private String paymentId;
  private String code;
  private String message;
  private String pgCode;
  private String pgMessage;
}
