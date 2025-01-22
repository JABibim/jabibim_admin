package com.jabibim.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value="/calendar")
public class CalendarController {

    @GetMapping(value="")
    public String boardList(
    ) {
        return "calendar/calendar5";
    }

}
