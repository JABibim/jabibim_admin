package com.jabibim.admin.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Course {
    private String courseId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private String courseName;
    private String courseSubject;
    private String courseInfo;
    private String courseTag;
    private String courseDiff;
    private int coursePrice;
    private boolean courseActivation;
    private String courseProfileOriginName;
    private String courseProfilePath;
    private String academyId;
    private String teacherId;
}
