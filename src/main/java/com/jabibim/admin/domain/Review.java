package com.jabibim.admin.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Review {

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

}
