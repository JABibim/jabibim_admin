package com.jabibim.admin.dto.content.course.request;

import lombok.Data;

@Data
public class updateCourseReqDto {
    private String courseId;
    private String courseName;
    private String courseSubject;
    private String courseInfo;
    private int coursePrice;
    private String courseTag;
    private String courseDiff;
}
