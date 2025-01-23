package com.jabibim.admin.dto.content.course.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SelectCourseListResDto {
    private int rowNum;
    private String courseId;
    private String courseName;
    private int classCount;
    private LocalDateTime createdAt;
    private String teacherName;
    private boolean courseActivation;
}
