package com.jabibim.admin.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Orders {

  private String ordersId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime deletedAt;
  private String orderNumber;
  private int totalPrice;
  private String orderAddress;
  private String orderDetailAddr;
  private String orderPostcode;
  private int orderStatus;
  private String studentId;
  private String courseId;
  private String academyId;
  private String paymentId;

}
