package com.jabibim.admin.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.jabibim.admin.dto.OrdersListVO;
import com.jabibim.admin.func.PaginationResult;
import com.jabibim.admin.service.OrdersService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OdersController {

  private final OrdersService ordersService;
  private final Logger logger = LoggerFactory.getLogger(OdersController.class);

  @GetMapping("/list")
  public ModelAndView list(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam(defaultValue = "") String orderDate1,
      @RequestParam(defaultValue = "") String orderDate2,
      @RequestParam(defaultValue = "") String paymentDate1,
      @RequestParam(defaultValue = "") String paymentDate2,
      @RequestParam(defaultValue = "all") String paymentStatus,
      @RequestParam(defaultValue = "all") String paymentMethod,
      @RequestParam(defaultValue = "nco") String searchField,
      @RequestParam(defaultValue = "") String searchWord,
      ModelAndView mv,
      Authentication auth) {

    HashMap<String, String> searchMap = new HashMap<>();
    searchMap.put("orderDate1", orderDate1);
    searchMap.put("orderDate2", orderDate2);
    searchMap.put("paymentDate1", paymentDate1);
    searchMap.put("paymentDate2", paymentDate2);
    searchMap.put("paymentStatus", paymentStatus);
    searchMap.put("paymentMethod", paymentMethod);
    searchMap.put("searchField", searchField);
    searchMap.put("searchWord", searchWord);

    int listcount = ordersService.getOrdersListCount(searchMap, auth);

    List<OrdersListVO> ordersList = ordersService.getOrdersList(page, limit, searchMap, auth);

    PaginationResult result = new PaginationResult(page, limit, listcount);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    Date orderStartDate = null;
    Date orderEndDate = null;
    Date paymentStartDate = null;
    Date paymentEndDate = null;

    try {
      if (!orderDate1.isEmpty()) {
        orderStartDate = sdf.parse(orderDate1);
      }
      if (!orderDate2.isEmpty()) {
        orderEndDate = sdf.parse(orderDate2);
      }
      if (!paymentDate1.isEmpty()) {
        paymentStartDate = sdf.parse(paymentDate1);
      }
      if (!paymentDate2.isEmpty()) {
        paymentEndDate = sdf.parse(paymentDate2);
      }
    } catch (ParseException e) {
      e.printStackTrace();
      logger.error("날짜 변환 오류");
    }

    mv.setViewName("orders/orders-list");
    mv.addObject("listcount", listcount);
    mv.addObject("ordersList", ordersList);
    mv.addObject("orderDate1", orderStartDate == null ? "" : orderStartDate);
    mv.addObject("orderDate2", orderEndDate == null ? "" : orderEndDate);
    mv.addObject("paymentDate1", paymentStartDate == null ? "" : paymentStartDate);
    mv.addObject("paymentDate2", paymentEndDate == null ? "" : paymentEndDate);
    mv.addObject("paymentStatus", paymentStatus);
    mv.addObject("paymentMethod", paymentMethod);
    mv.addObject("searchField", searchField);
    mv.addObject("searchWord", searchWord);
    mv.addObject("page", page);
    mv.addObject("limit", limit);
    mv.addObject("maxpage", result.getMaxpage());
    mv.addObject("startpage", result.getStartpage());
    mv.addObject("endpage", result.getEndpage());

    return mv;
  }

}
