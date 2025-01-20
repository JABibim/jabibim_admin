package com.jabibim.admin.controller;


import com.jabibim.admin.dto.CourseListDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
@RequestMapping(value="/calendar")
public class CalendarController {

    @GetMapping(value="")
    public String boardList(
    ) {
        return "calendar/calendar3";
    }
}
