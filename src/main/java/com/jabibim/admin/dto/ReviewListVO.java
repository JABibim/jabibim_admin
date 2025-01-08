package com.jabibim.admin.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewListVO {

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
  private String teacherName;
  private String studentName;
  private String studentEmail;
  private int replyStatus;

}
