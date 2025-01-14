package com.jabibim.admin.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TeacherProfileDTO {
    private String teacherName;
    private String academyName;
    private String teacherJob;
    private String teacherEmail;
    private String teacherPhone;
    private String teacherImgName;
}
