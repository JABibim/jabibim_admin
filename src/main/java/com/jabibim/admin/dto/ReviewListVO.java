package com.jabibim.admin.dto;

import com.jabibim.admin.domain.Review;

public class ReviewListVO extends Review {
  private String teacherName;
  private String studentName;
  private String studentEmail;
  private int replyStatus;

  public ReviewListVO() {}

  public String getTeacherName() {
    return teacherName;
  }

  public void setTeacherName(String teacherName) {
    this.teacherName = teacherName;
  }

  public String getStudentName() {
    return studentName;
  }

  public void setStudentName(String studentName) {
    this.studentName = studentName;
  }

  public String getStudentEmail() {
    return studentEmail;
  }

  public void setStudentEmail(String studentEmail) {
    this.studentEmail = studentEmail;
  }

  public int getReplyStatus() {
    return replyStatus;
  }

  public void setReplyStatus(int replyStatus) {
    this.replyStatus = replyStatus;
  }
}
