package com.jabibim.admin.controller;

import com.google.gson.JsonObject;
import com.jabibim.admin.domain.Review;
import com.jabibim.admin.dto.ReviewDetailVO;
import com.jabibim.admin.func.PaginationResult;
import com.jabibim.admin.dto.ReviewListVO;
import com.jabibim.admin.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
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

//    for (ReviewListVO reviewListVO : list) {
//      logger.info(reviewListVO.toString());
//    }

    PaginationResult result =new PaginationResult(page, limit, listcount);

    // 날짜 전환을 위한 SimpleDateFormat
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    Date start = null;
    Date end = null;
    
    try {
      if (!reviewDate1.isEmpty()) {
        // input type=date 를 위해 날짜타입으로 전환
        start = sdf.parse(reviewDate1);
      }
      if (!reviewDate2.isEmpty()) {
        // input type=date 를 위해 날짜타입으로 전환
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

  @PostMapping("/detail/replyupdate")
  public String updateReply(Review reply) {

    return null;
  }

  @PostMapping("/detail/reviewupdate")
  public String updateReview(Review review) {

    return null;
  }

  @GetMapping("/detail")
  public ModelAndView reviewDetail(@RequestParam(required=true) String reviewid, ModelAndView mv, Authentication auth) {


    List<ReviewDetailVO> list = reviewService.getReviewDetails(reviewid);

    // 어드민 계정이면 isAdmin 값 임시 부여
    // 이후 ROLE 값 결정되면 타임리프에서 ROLE 값을 통해 양식 결정
    for (GrantedAuthority authority : auth.getAuthorities()) {
      if (authority.getAuthority().equals("ROLE_ADMIN")) {
        mv.addObject("isAdmin", true);
      }
    }
//    for(ReviewDetailVO vo : list) {
//      logger.info(vo.toString());
//    }
    mv.setViewName("review/reviewDetail");
    mv.addObject("detaillist", list);
    return mv;
  }

  @PostMapping("/detail/reply")
  public String InsertReply(Review reply, RedirectAttributes rattr, HttpServletRequest request) {

    String originId = reply.getReviewId();

    boolean result = reviewService.insertReply(reply);
    if (result) {
      logger.info(reply.toString());
      rattr.addFlashAttribute("message", "답변이 정상적으로 등록되었습니다.");
      rattr.addFlashAttribute("url", request.getContextPath() + "/board/review/detail?reviewid=" + originId);
    } else {
      rattr.addFlashAttribute("message", "답변 등록에 실패했습니다.");
      rattr.addFlashAttribute("url", "history.back()");
    }
      return "redirect:/message";
  }

  @PostMapping("/detail/reviewdelete")
  @ResponseBody
  public String deleteReview(@RequestParam(required=true) String reviewId
                             ,HttpServletRequest request
                             ,HttpServletResponse response
                             ,ModelMap model) throws Exception {
    JsonObject jsonObject = new JsonObject();

    int result = reviewService.deleteReview(reviewId);
    if (result > 0) {
      jsonObject.addProperty("message", "삭제에 성공했습니다.");
      jsonObject.addProperty("url",request.getContextPath() + "/board/review/list");
    } else {
      jsonObject.addProperty("message", "삭제에 실패했습니다.");
      jsonObject.addProperty("url", "location.reload(true)");
    }

    response.setContentType("text/html;charset=utf-8");
    PrintWriter out = response.getWriter();
    out.write(jsonObject.toString());

    return null;
  }

  @PostMapping("/detail/replydelete")
  @ResponseBody
  public String deleteReply(@RequestParam(required=true) String replyId
      , @RequestParam(required=true) String replyPassword
      ,HttpServletRequest request
      ,HttpServletResponse response
      ,ModelMap model) throws Exception {
    JsonObject jsonObject = new JsonObject();

    int result = reviewService.deleteReply(replyId, replyPassword);
    if (result > 0) {
      jsonObject.addProperty("message", "삭제에 성공했습니다.");
    } else if (result == 0) {
      jsonObject.addProperty("message", "삭제에 실패했습니다.");
    } else {
      jsonObject.addProperty("message", "비밀번호가 일치하지 않습니다.");
    }
      jsonObject.addProperty("url", "location.reload(true)");

    response.setContentType("text/html;charset=utf-8");
    PrintWriter out = response.getWriter();
    out.write(jsonObject.toString());

    return null;
  }

  }
