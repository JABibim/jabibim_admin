package com.jabibim.admin.dto;

import java.time.LocalDateTime;
import java.util.List;

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
public class CourseDetailVO {
  private String courseId;
  private LocalDateTime createdAt;
  private String courseName;
  private String courseSubject;
  private String courseInfo;
  private String courseTag;
  private String courseDiff;
  private int coursePrice;
  private String courseProfileOriginName;
  private String courseProfilePath;
  private String academyId;
  private String teacherId;
  private String teacherName;
  private int classCount;
  private List<ClassInfoVO> classList;

}
