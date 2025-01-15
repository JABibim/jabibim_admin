package com.jabibim.admin.dto.content.course.request;

import lombok.Data;

@Data
public class SelectCourseListReqDto {
    private String useStatus;
    private int searchCondition;
    private String searchKeyword;
    private String startDate;
    private String endDate;
    private int page;
}
