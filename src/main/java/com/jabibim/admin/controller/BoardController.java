package com.jabibim.admin.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/board")
public class BoardController {


    @GetMapping(value="/notice")
    public String boardList(
           ) {
        return "board/notice/notice_list";
    }

    @GetMapping(value="/notice/detail")
    public String boardDetail() {
        return "board/notice/notice_detail";
    }

    @GetMapping(value="/notice/modify")
    public String boardModify() {
        return "board/notice/notice_modify";
    }

    @GetMapping(value="/notice/write")
    public String boardWrite() {
        return "board/notice/notice_write";
    }

    @GetMapping(value="/qna/detail")
    public String qnaDetail() {
        return "board/qna/qnaDetail";
    }

    @GetMapping(value="/qna")
    public String qnaList() {
        return "board/qna/qnaList";
    }



}
