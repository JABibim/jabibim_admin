package com.jabibim.admin.domain;

import lombok.Data;

import java.util.Date;

@Data
public class TeacherCareer {
    private String careerId;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private String careerName;
    private String careerInfo;
    private String careerFileName;
    private String careerFileOrigin;
    private int displayStatus;
    private String teacherId;
    private String academyId;
}