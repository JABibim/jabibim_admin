package com.jabibim.admin.dto;

import com.google.gson.JsonObject;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResignListVO {

  private String studentId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime deletedAt;
  private LocalDateTime deletionDate;
  private String studentName;
  private String studentEmail;
  private String studentPhone;
  private String studentPassword;
  private JsonObject studentAddress;
  private int verification;
  private String studentImgName;
  private String studentImgOrigin;
  private int adsAgreed;
  private String authRole;
  private String gradeId;
  private String academyId;


}
