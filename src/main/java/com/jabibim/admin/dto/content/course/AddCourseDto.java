package com.jabibim.admin.dto.content.course;

import lombok.Data;

@Data
public class AddCourseDto {
    private String courseName; // 과정명
    private String courseSubject; // 과목명
    private String courseIntro; // 과정 상세 설명
    private int coursePrice; // 수강 금액
    private String courseTag; // 과정 태그
    private String courseDiff; // 과정 난이도
}
