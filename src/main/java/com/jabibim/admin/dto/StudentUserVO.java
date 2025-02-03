package com.jabibim.admin.dto;

import com.google.gson.JsonObject;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentUserVO {

  private String studentId;
  private Date createdAt;
  private Date updatedAt;
  private Date deletedAt;
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
