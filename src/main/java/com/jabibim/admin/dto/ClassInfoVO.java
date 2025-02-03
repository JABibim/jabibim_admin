package com.jabibim.admin.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClassInfoVO {
  private String classId;
  private LocalDateTime createdAt;
  private String className;
  private String classContent;
  private int classSeq;
  private String classType;
  private String academyId;
  private String teacherId;
  private String teacherName;
  private String courseId;
}
