package com.jab.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionController {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(Exception.class)
    public ModelAndView common(Exception e, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();

        if (e.getMessage().contains("No static resource")) {
            mav.setViewName("error/404");
        } else {
            mav.setViewName("error/common");
        }

        mav.addObject("exception", e);
        mav.addObject("url", request.getRequestURL());

        logger.info("📍 Exception 📍 :: " + e);

        return mav;
    }
}
