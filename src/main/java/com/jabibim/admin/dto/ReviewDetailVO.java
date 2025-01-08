package com.jabibim.admin.dto;

import com.jabibim.admin.domain.Review;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDetailVO {

  private String reviewId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime deletedAt;
  private int reviewReRef;
  private int reviewReLev;
  private String reviewSubject;
  private String reviewPassword;
  private String reviewContent;
  private int reviewRating;
  private int reviewReadcount;
  private int reviewExposureStat;
  private String academyId;
  private String courseId;
  private String teacherId;
  private String studentId;
  private String teacherEmail;
  private String teacherPhone;
  private String teacherName;
  private String studentEmail;
  private String studentName;
  private String courseName;
}
