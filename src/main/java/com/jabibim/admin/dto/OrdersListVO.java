package com.jabibim.admin.dto;

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
public class OrdersListVO {

  private String ordersId;
  private LocalDateTime createdAt;
  private String ordersNumber;
  private int totalPrice;
  private int ordersStatus;
  private String studentId;
  private String studentName;
  private String courseId;
  private String courseName;
  private String paymentId;
  private String paymentMethod;
  private String paymentStatus;
  private int paymentAmount;
  private String paidAt;
  private String academyId;
  private String academyName;
}