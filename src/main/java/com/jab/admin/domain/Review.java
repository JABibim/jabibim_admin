package com.jab.admin.domain;

import java.time.LocalDateTime;

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


  public Review() {}

  public String getReviewId() {
    return reviewId;
  }

  public void setReviewId(String reviewId) {
    this.reviewId = reviewId;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public LocalDateTime getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(LocalDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }

  public int getReviewReRef() {
    return reviewReRef;
  }

  public void setReviewReRef(int reviewReRef) {
    this.reviewReRef = reviewReRef;
  }

  public int getReviewReLev() {
    return reviewReLev;
  }

  public void setReviewReLev(int reviewReLev) {
    this.reviewReLev = reviewReLev;
  }

  public String getReviewSubject() {
    return reviewSubject;
  }

  public void setReviewSubject(String reviewSubject) {
    this.reviewSubject = reviewSubject;
  }

  public String getReviewPassword() {
    return reviewPassword;
  }

  public void setReviewPassword(String reviewPassword) {
    this.reviewPassword = reviewPassword;
  }

  public String getReviewContent() {
    return reviewContent;
  }

  public void setReviewContent(String reviewContent) {
    this.reviewContent = reviewContent;
  }

  public int getReviewRating() {
    return reviewRating;
  }

  public void setReviewRating(int reviewRating) {
    this.reviewRating = reviewRating;
  }

  public int getReviewReadcount() {
    return reviewReadcount;
  }

  public void setReviewReadcount(int reviewReadcount) {
    this.reviewReadcount = reviewReadcount;
  }

  public int getReviewExposureStat() {
    return reviewExposureStat;
  }

  public void setReviewExposureStat(int reviewExposureStat) {
    this.reviewExposureStat = reviewExposureStat;
  }

  public String getAcademyId() {
    return academyId;
  }

  public void setAcademyId(String academyId) {
    this.academyId = academyId;
  }

  public String getCourseId() {
    return courseId;
  }

  public void setCourseId(String courseId) {
    this.courseId = courseId;
  }

  public String getTeacherId() {
    return teacherId;
  }

  public void setTeacherId(String teacherId) {
    this.teacherId = teacherId;
  }

  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }
}
