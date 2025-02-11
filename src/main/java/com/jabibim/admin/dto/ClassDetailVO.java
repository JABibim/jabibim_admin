package com.jabibim.admin.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClassDetailVO implements Serializable {
    private String classId;
    private String className;
    private String classContent;
    private int classSeq;
    private String classType;
    private String classFileId;
    private String classFileOriginName;
    private String classFilePath;
    private String classFileType;
    private Long classFileSize;
}
