package com.jabibim.admin.front.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentVO {
  // 포트원 결제 결과를 받을 때 사용하는 dto
  private String paymentId;
  private String studentId;
  private String academyId;
}
