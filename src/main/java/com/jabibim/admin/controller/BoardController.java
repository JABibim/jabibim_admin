package com.jabibim.admin.controller;


import com.jabibim.admin.domain.Board;
import com.jabibim.admin.dto.CourseListDTO;
import com.jabibim.admin.func.UUIDGenerator;
import com.jabibim.admin.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value="/board")
public class BoardController {

    @Value("${upload.path}")
    private String saveFolder;

    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    private BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping(value="/notice")
    public String boardList(
           ) {
        return "board/notice/notice_list";
    }

    @GetMapping(value="/notice/write")
    public ModelAndView boardWrite(ModelAndView mv) {
        List<CourseListDTO> course = boardService.getCourseList();

        mv.setViewName("board/notice/notice_write");
        mv.addObject("courselist", course);
        return mv;
    }

    @PostMapping(value="/notice/add")
    public String addNotice(Board notice, HttpServletRequest request) throws Exception {

        MultipartFile uploadfile = notice.getUploadfile();
        if(!uploadfile.isEmpty()){
            String fileDBName = boardService.saveUploadedFile(uploadfile, saveFolder);
            notice.setBoardFileName(fileDBName); //바뀐 파일명으로 저장
            notice.setBoardFileOrigin(uploadfile.getOriginalFilename());//원래 파일명 저장
        }

        // 생성한 UUID를 Privacy 객체에 설정
        notice.setBoardId(UUIDGenerator.getUUID());
        boardService.insertNotice(notice);
        logger.info(notice.toString());
        return "redirect:/board/notice";
    }

    @GetMapping(value="/notice/detail")
    public String boardDetail() {
        return "board/notice/notice_detail";
    }

    @GetMapping(value="/notice/modify")
    public String boardModify() {
        return "board/notice/notice_modify";
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
