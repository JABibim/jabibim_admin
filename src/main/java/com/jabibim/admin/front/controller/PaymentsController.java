package com.jabibim.admin.front.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/payments")
public class PaymentsController {

  private final Logger logger = LoggerFactory.getLogger(PaymentsController.class);

  @PostMapping("/pre-register")
  public String preRegister(@RequestParam(required = true) String id) {
    return null;
  }

}
