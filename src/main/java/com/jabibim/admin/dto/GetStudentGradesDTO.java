package com.jabibim.admin.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetStudentGradesDTO {

    private String rnum;
    private String gradeName;
    private String discountRate;
    private String studentCount;
    private String gradeId;
    private String academyId;
}
