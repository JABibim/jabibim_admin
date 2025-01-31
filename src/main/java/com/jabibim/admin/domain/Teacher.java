package com.jabibim.admin.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Teacher {
    private String teacherId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private String teacherName;
    private String teacherPhone;
    private String teacherEmail;
    private String teacherPassword;
    private String teacherJob;
    private String teacherProfileOriginName;
    private String teacherProfilePath;
    private String authRole;
    private String academyId;
}