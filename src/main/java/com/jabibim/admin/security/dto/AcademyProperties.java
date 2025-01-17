package com.jabibim.admin.security.dto;

import lombok.Getter;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;  // 이 import 추가

@Component
@PropertySource("/properties/academyinfo.properties")
@Getter
public class AcademyProperties {

  @Value("${academy.secret}")
  private String secretKey;

  @Value("${academy.id}")
  private String academyId;
}