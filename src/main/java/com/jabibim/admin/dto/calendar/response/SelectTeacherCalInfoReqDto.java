package com.jabibim.admin.dto.calendar.response;

import lombok.Data;

@Data
public class SelectTeacherCalInfoReqDto {
    private String calendarId;
    private String googleCalendarId;
}