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

  @NotBlank(message = "이메일은 필수입니다.") // 공백, null, 빈문자열 불가
  private String email;


  @NotBlank(message = "비밀번호는 필수입니다.") // 공백, null, 빈문자열 불가
  private String password;
}
