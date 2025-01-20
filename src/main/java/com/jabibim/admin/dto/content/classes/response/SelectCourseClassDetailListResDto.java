package com.jabibim.admin.dto.content.classes.response;

import lombok.Data;

@Data
public class SelectCourseClassDetailListResDto {
    private int num;
    private String classId;
    private String className;
    private String teacherName;
    private int hasVideo;
    private int hasFile;
}
