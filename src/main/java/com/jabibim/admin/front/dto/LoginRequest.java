package com.jabibim.admin.front.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequest implements Serializable {

  public LoginRequest(String email, String academyId) {
    this.email = email;
    this.academyId = academyId;
  }
  // 로그인 요청 body 에 들어 있는 값을 받는 dto

  @NotBlank(message = "이메일은 필수입니다.") // 공백, null, 빈문자열 불가
  private String email;

  @NotBlank(message = "비밀번호는 필수입니다.") // 공백, null, 빈문자열 불가
  private String password;

  @NotBlank(message = "학원 아이디는 필수입니다.") // 공백, null, 빈문자열 불가
  private String academyId;
}
