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
public class OrdersDetailVO {

  private String ordersId;
  private LocalDateTime createdAt;
  private String ordersNumber;
  private int totalPrice;
  private String ordersAddress;
  private String ordersDetailAddr;
  private String ordersPostcode;
  private int ordersStatus;
  private String studentId;
  private String studentName;
  private String studentPhone;
  private String studentEmail;
  private String gradeId;
  private String gradeName;
  private int discountRate;
  private String courseId;
  private String courseName;
  private String coursePrice;
  private String paymentId;
  private String paidAt;
  private String paymentAmount;
  private String paymentMethod;
  private String paymentStatus;
  private String academyId;

}
