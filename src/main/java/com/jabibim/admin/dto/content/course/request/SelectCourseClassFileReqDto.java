package com.jabibim.admin.dto.content.course.request;

import lombok.Data;

@Data
public class SelectCourseClassFileReqDto {
    private String classFileId;
    private String classFileName;
    private String classFileOriginName;
    private String classFilePath;
    private String classFileType;
    private String classFileSize;
}