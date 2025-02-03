package com.jabibim.admin.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderApiVO {
  private String ordersId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime deletedAt;
  private String studentId;
  private String studentName;
  private String studentEmail;
  private String courseId;
  private String courseName;
  private int orderPrice;
  private String paymentId;
  private int paymentStatus;
  private int paymentAmount;
  private String academyId;

}
