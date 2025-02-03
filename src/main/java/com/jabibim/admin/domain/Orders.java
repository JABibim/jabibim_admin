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
  private String ordersNumber;
  private int totalPrice;
  private String ordersAddress;
  private String ordersDetailAddr;
  private String ordersPostcode;
  private int ordersStatus;
  private String studentId;
  private String courseId;
  private String academyId;
  private String paymentId;

}
