package com.jabibim.admin.dto.content.course;

import lombok.Data;

@Data
public class GetCourseListDto {
    private String useStatus;
    private int searchCondition;
    private String searchKeyword;
    private String startDate;
    private String endDate;
    private int page;
    private int limit;
    private String state;
}
