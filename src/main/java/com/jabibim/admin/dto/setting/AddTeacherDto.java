package com.jabibim.admin.dto.setting;

import com.jabibim.admin.domain.AuthRole;
import lombok.Data;

@Data
public class AddTeacherDto {
    private String academyId;
    private String teacherName;
    private String teacherPhone;
    private String teacherEmail;
    private String teacherPassword;
    private AuthRole authRole;
}
