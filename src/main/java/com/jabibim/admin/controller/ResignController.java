package com.jabibim.admin.controller;

import com.jabibim.admin.dto.ResignListVO;
import com.jabibim.admin.func.PaginationResult;
import com.jabibim.admin.service.ResignedStudentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/student/resignhist")
@RequiredArgsConstructor
public class ResignController {


  private final Logger logger = LoggerFactory.getLogger(ResignController.class);
  private final ResignedStudentService studentResignService;


  @GetMapping("/list")
  public ModelAndView resignHistList(
      ModelAndView mv
      , @RequestParam(defaultValue="1") int page
      , @RequestParam(defaultValue="10") int limit
      , @RequestParam(defaultValue="") String resignDate1
      , @RequestParam(defaultValue="") String resignDate2
      , @RequestParam(defaultValue="withdrawal") String dateField
      , @RequestParam(defaultValue="nerd") String searchField
      , @RequestParam(defaultValue="") String searchWord
      , Authentication auth) {

    HashMap<String,String> hm = new HashMap<>();
    hm.put("resignDate1", resignDate1);
    hm.put("resignDate2", resignDate2);
    hm.put("dateField", dateField);
    hm.put("searchField", searchField);
    hm.put("searchWord", searchWord);

    int listcount = studentResignService.getResignedStudentCount(hm, auth);

    logger.info("listcount: " + listcount);

    List<ResignListVO> list = studentResignService.getResignedStudentList(hm, page, limit, auth);

    for (ResignListVO resignListVO : list) {
      logger.info("resignListVO: " + resignListVO.toString());
    }

    PaginationResult result = new PaginationResult(page, limit, listcount);

    // 날짜 전환을 위한 SimpleDateFormat
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    Date start = null;
    Date end = null;

    try {
      if (!resignDate1.isEmpty()) {
        start = sdf.parse(resignDate1);
      }
      if (!resignDate2.isEmpty()) {
        end = sdf.parse(resignDate2);
      }
    } catch(ParseException pe) {
      logger.error("날짜 전환 에러 " + pe.getMessage());
    }

    mv.setViewName("students/studentresignhist");
    mv.addObject("listcount", listcount);
    mv.addObject("page", page);
    mv.addObject("limit", limit);
    mv.addObject("studentlist", list);
    mv.addObject("maxpage", result.getMaxpage());
    mv.addObject("startpage", result.getStartpage());
    mv.addObject("endpage", result.getEndpage());
    mv.addObject("resignDate1", start != null ? start : "");
    mv.addObject("resignDate2", end != null ? end : "");
    mv.addObject("dateField", dateField);
    mv.addObject("searchField", searchField);
    mv.addObject("searchWord", searchWord);

    return mv;
  }



}
