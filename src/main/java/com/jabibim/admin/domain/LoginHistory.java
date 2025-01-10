package com.jabibim.admin.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginHistory {

  private String loginHistoryId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime deletedAt;
  private String ipInfo;
  private String osInfo;
  private String browserInfo;
  private int loginSuccess;
  private String academyId;
  private String studentId;
}
