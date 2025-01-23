package com.jabibim.admin.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartItemVO {

  private String cartId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime deletedAt;
  private String studentId;
  private String studentName;
  private String courseId;
  private String courseName;
  private String courseSubject;
  private String courseDiff;
  private int coursePrice;
  private String courseImgName;
  private String academyId;
  private String teacherName;

}
