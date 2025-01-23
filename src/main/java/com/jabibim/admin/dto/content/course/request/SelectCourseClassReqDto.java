package com.jabibim.admin.dto.content.course.request;

import lombok.Data;

@Data
public class SelectCourseClassReqDto {
    private String classId;
    private int classSeq;
    private String className;
    private String classContent;
    private String classType;
}