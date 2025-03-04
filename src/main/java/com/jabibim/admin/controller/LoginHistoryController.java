package com.jabibim.admin.controller;

import com.jabibim.admin.dto.LoginHistListVO;
import com.jabibim.admin.func.PaginationResult;
import com.jabibim.admin.service.LoginHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
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
@RequestMapping("/student/signhist")
@RequiredArgsConstructor
public class LoginHistoryController {

  private final Logger logger = LoggerFactory.getLogger(LoginHistoryController.class);

  private final LoginHistoryService loginHistoryService;

  @GetMapping("/list")
  public ModelAndView signInHistList(ModelAndView mv
                                     , @RequestParam(defaultValue="1") int page
                                     , @RequestParam(defaultValue="10") int limit
                                     , @RequestParam(defaultValue="") String signDate1
                                     , @RequestParam(defaultValue="") String signDate2
                                     , @RequestParam(defaultValue="sf") String logStatus
                                     , @RequestParam(defaultValue="enboit") String searchField
                                     , @RequestParam(defaultValue="") String searchWord
                                     , HttpServletRequest request) {

    HttpSession session = request.getSession();

    HashMap<String, String> hm = new HashMap<>();
    hm.put("signDate1", signDate1);
    hm.put("signDate2", signDate2);
    hm.put("logStatus", logStatus);
    hm.put("searchField", searchField);
    hm.put("searchWord", searchWord);


    int listcount = loginHistoryService.getLoginHistCount(hm, session);

    List<LoginHistListVO> list = loginHistoryService.getLoginHistList(hm, page, limit, session);


    // 날짜 전환을 위한 SimpleDateFormat
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    Date start = null;
    Date end = null;

    try {
      if (!signDate1.isEmpty()) {
        start = sdf.parse(signDate1);
      }
      if (!signDate2.isEmpty()) {
        end = sdf.parse(signDate2);
      }
    } catch(ParseException pe) {
      logger.error("날짜 전환 에러 " + pe.getMessage());
    }





    PaginationResult result = new PaginationResult(page, limit, listcount);

    mv.addObject("listcount", listcount);
    mv.addObject("studentlist", list);
    mv.addObject("page", page);
    mv.addObject("maxpage", result.getMaxpage());
    mv.addObject("startpage", result.getStartpage());
    mv.addObject("endpage", result.getEndpage());
    mv.addObject("limit", limit);
    mv.addObject("signDate1", start != null ? start : "");
    mv.addObject("signDate2", end != null ? end : "");
    mv.addObject("logStatus", logStatus);
    mv.addObject("searchField", searchField);
    mv.addObject("searchWord", searchWord);
    mv.setViewName("students/studentsignhist");

    return mv;
  }
}
