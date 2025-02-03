package com.jabibim.admin.front.dto;

import java.io.Serializable;

public class LoginRequest implements Serializable {

  public LoginRequest() {
  }

  public LoginRequest(String email, String academyId) {
    this.email = email;
    this.academyId = academyId;
  }

  public LoginRequest(String email, String password, String academyId) {
    this.email = email;
    this.password = password;
    this.academyId = academyId;
  }
  // 로그인 요청 body 에 들어 있는 값을 받는 dto

  private String email;

  private String password;

  private String academyId;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getAcademyId() {
    return academyId;
  }

  public void setAcademyId(String academyId) {
    this.academyId = academyId;
  }

}
