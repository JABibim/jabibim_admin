package com.jab.admin.controller;

import com.jab.admin.func.PaginationResult;
import com.jab.admin.dto.ReviewListVO;
import com.jab.admin.service.ReviewService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@RequestMapping("/board/review")
public class ReviewController {

  private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

  private ReviewService reviewService;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public ReviewController(ReviewService reviewService, PasswordEncoder passwordEncoder) {
    this.reviewService = reviewService;
    this.passwordEncoder = passwordEncoder;
  }



  // 그냥 검색 조건 없이 리뷰 목록 출력
  @GetMapping("/list")
  public ModelAndView reviewList(ModelAndView mv, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit,
                           @RequestParam(defaultValue="") String reviewDate1,
                           @RequestParam(defaultValue="") String reviewDate2,
                           @RequestParam(defaultValue="all") String reply_status,
                           @RequestParam(defaultValue="all") String rating,
                           @RequestParam(defaultValue="all") String review_visible,
                           @RequestParam(defaultValue="snect") String review_searchField,
                           @RequestParam(defaultValue="") String search_word) {

    // 검색조건 hashmap 에 저장하여 서비스에 전달
    HashMap<String, String> hm = new HashMap<>();
    hm.put("reviewDate1", reviewDate1);
    hm.put("reviewDate2", reviewDate2);
    hm.put("reply_status", reply_status);
    hm.put("rating", rating);
    hm.put("review_visible", review_visible);
    hm.put("review_searchField", review_searchField);
    hm.put("search_word", search_word);

    int listcount = reviewService.getSearchListCount(hm);
    logger.info("listcount: " + listcount);

    List<ReviewListVO> list = reviewService.getSearchList(hm, page, limit);

    for (ReviewListVO reviewListVO : list) {
      logger.info(reviewListVO.getStudentName());
    }

    PaginationResult result =new PaginationResult(page, limit, listcount);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    Date start = null;
    Date end = null;
    try {
      if (!reviewDate1.isEmpty()) {
        start = sdf.parse(reviewDate1);
      }
      if (!reviewDate2.isEmpty()) {
        end = sdf.parse(reviewDate2);
      }
    } catch (ParseException pe) {
      logger.error("날짜 전환 에러" + pe.getMessage());
    }



    mv.setViewName("review/index");
    mv.addObject("page", page);
    mv.addObject("limit", limit);
    mv.addObject("maxpage", result.getMaxpage());
    mv.addObject("startpage", result.getStartpage());
    mv.addObject("endpage", result.getEndpage());
    mv.addObject("listcount", listcount);
    mv.addObject("reviewlist", list);
    mv.addObject("reviewDate1", start == null ? "" : start);
    mv.addObject("reviewDate2", end == null ? "" : end);
    mv.addObject("reply_status", reply_status);
    mv.addObject("rating", rating);
    mv.addObject("review_visible", review_visible);
    mv.addObject("review_searchField", review_searchField);
    mv.addObject("search_word", search_word);

    return mv;
  }
}
