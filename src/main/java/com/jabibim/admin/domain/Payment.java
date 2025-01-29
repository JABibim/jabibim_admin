package com.jabibim.admin.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

  // paymentId 가 중복 가능하므로 ordersId 와 복합키를 만들어서 사용할 수도 있다.
  private String paymentId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime deletedAt;
  private int paymentAmount;
  private String paymentMethod;
  private String paymentStatus;
  private String paidAt;
  private String receiptUrl;
  private String studentId;
  private String ordersId;
  private String courseId;
  private String academyId;

}
