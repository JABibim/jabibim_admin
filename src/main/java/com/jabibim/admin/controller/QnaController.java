package com.jabibim.admin.controller;

import com.jabibim.admin.domain.PaginationResult;
import com.jabibim.admin.domain.Qna;
import com.jabibim.admin.func.UUIDGenerator;
import com.jabibim.admin.service.QnaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="/board")
public class QnaController {

    @Value("${upload.path}")
    private String saveFolder;

    private static final Logger logger = LoggerFactory.getLogger(QnaController.class);

    private QnaService qnaService;

    public QnaController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @GetMapping(value="/qna")
    public ModelAndView qnaList(
            @RequestParam(defaultValue = "1") int page,
            ModelAndView mv,
            HttpSession session
    ){
        session.setAttribute("referer", "notice");
        session.setAttribute("page", page);
        int limit =10;

        String academyId = (String) session.getAttribute("aid");

        int listcount = qnaService.getListCount(academyId);

        List<Qna> list = qnaService.getQnaList(page, limit, academyId);

        PaginationResult result = new PaginationResult(page, limit, listcount);

        mv.setViewName("qna/qnaList");
        mv.addObject("page", page);
        mv.addObject("maxpage", result.getMaxpage());
        mv.addObject("startpage", result.getStartpage());
        mv.addObject("endpage", result.getEndpage());
        mv.addObject("listcount", listcount);
        mv.addObject("qnaList", list);
        mv.addObject("limit",limit);
        return mv;
    }

    @ResponseBody
    @PostMapping(value="/qna_list_ajax")
    public Map<String, Object> QnaListAjax(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            HttpSession session
    ){

        String academyId = (String) session.getAttribute("aid");
        int listcount = qnaService.getListCount(academyId); //총 리스트 수를 받아옴
        List<Qna> list = qnaService.getQnaList(page, limit, academyId); // 리스트를 받아옴
        PaginationResult result = new PaginationResult(page, limit, listcount);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("maxpage", result.getMaxpage());
        map.put("startpage", result.getStartpage());
        map.put("endpage", result.getEndpage());
        map.put("listcount", listcount);
        map.put("qnaList", list);
        map.put("limit",limit);

        System.out.println("Returned JSON Data: " + map);
        return map;
    }

    @GetMapping(value="/qna/detail")
    public String qnaDetail() {
        return "qna/qnaDetail";
    }

}
