package com.jabibim.admin.front.properties;

import lombok.Getter;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;  // 이 import 추가

@Component
@PropertySource("/properties/academyinfo.properties")
@Getter
public class AcademyProperties {
  // 학원 id 를 넘겨 받기 위한 비밀키 관리하는 클래스
  private String secretKey;
  private String academyId;

  public AcademyProperties(@Value("${academy.secret}") String secretKey,   @Value("${academy.id}") String academyId) {
    this.secretKey = secretKey;
    this.academyId = academyId;
  }
}