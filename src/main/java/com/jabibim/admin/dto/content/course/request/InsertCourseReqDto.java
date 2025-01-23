package com.jabibim.admin.dto.content.course.request;

import lombok.Data;

@Data
public class InsertCourseReqDto {
    private String courseId; // 과정 ID
    private String courseName; // 과정명
    private String courseSubject; // 과목명
    private String courseInfo; // 과정 상세 설명
    private int coursePrice; // 수강 금액
    private String courseTag; // 과정 태그
    private String courseDiff; // 과정 난이도
    private String courseProfileOriginName; // 과정 대표 이미지 파일명
    private String courseProfilePath; // 과정 대표 이미지 경로 (S3 경로)
    private String academyId; // 학원 ID
    private String teacherId; // 강사 ID
}
