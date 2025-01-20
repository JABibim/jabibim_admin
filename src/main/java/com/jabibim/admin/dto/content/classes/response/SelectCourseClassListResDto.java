package com.jabibim.admin.dto.content.classes.response;

import lombok.Data;

@Data
public class SelectCourseClassListResDto {
    private String courseId;
    private String courseName;
    private int courseClassCount;
}
