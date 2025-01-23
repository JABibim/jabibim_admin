package com.jabibim.admin.domain;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Board {
    private int rnum;
    private String boardId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private int boardReRef;
    private int boardReLev;
    private int boardReSeq;
    private int boardNotice;
    private String boardSubject;
    private String boardPassword;
    private String boardContent;
    private String boardFileName;
    private String boardFileOrigin;
    private int boardReadCount;
    private int boardExposureStat;
    private String boardTypeId;
    private String studentId;
    private String studentName;
    private String teacherId;
    private String teacherName;
    private String courseId;
    private MultipartFile uploadfile;
    private String academyId;
    private String courseName;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getAcademyId() {
        return academyId;
    }

    public void setAcademyId(String academyId) {
        this.academyId = academyId;
    }

    public int getRnum() {
        return rnum;
    }

    public void setRnum(int rnum) {
        this.rnum = rnum;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public int getBoardReRef() {
        return boardReRef;
    }

    public void setBoardReRef(int boardReRef) {
        this.boardReRef = boardReRef;
    }

    public int getBoardReLev() {
        return boardReLev;
    }

    public void setBoardReLev(int boardReLev) {
        this.boardReLev = boardReLev;
    }

    public int getBoardReSeq() {
        return boardReSeq;
    }

    public void setBoardReSeq(int boardReSeq) {
        this.boardReSeq = boardReSeq;
    }


    public String getBoardContent() {
        return boardContent;
    }

    public void setBoardContent(String boardContent) {
        this.boardContent = boardContent;
    }

    public int getBoardReadCount() {
        return boardReadCount;
    }

    public void setBoardReadCount(int boardReadCount) {
        this.boardReadCount = boardReadCount;
    }

    public int getBoardExposureStat() {
        return boardExposureStat;
    }

    public void setBoardExposureStat(int boardExposureStat) {
        this.boardExposureStat = boardExposureStat;
    }

    public String getBoardTypeId() {
        return boardTypeId;
    }

    public void setBoardTypeId(String boardTypeId) {
        this.boardTypeId = boardTypeId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getBoardNotice() {
        return boardNotice;
    }

    public void setBoardNotice(int boardNotice) {
        this.boardNotice = boardNotice;
    }

    public String getBoardSubject() {
        return boardSubject;
    }

    public void setBoardSubject(String boardSubject) {
        this.boardSubject = boardSubject;
    }

    public String getBoardPassword() {
        return boardPassword;
    }

    public void setBoardPassword(String boardPassword) {
        this.boardPassword = boardPassword;
    }

    public String getBoardFileName() {
        return boardFileName;
    }

    public void setBoardFileName(String boardFileName) {
        this.boardFileName = boardFileName;
    }

    public String getBoardFileOrigin() {
        return boardFileOrigin;
    }

    public void setBoardFileOrigin(String boardFileOrigin) {
        this.boardFileOrigin = boardFileOrigin;
    }

    public MultipartFile getUploadfile() {
        return uploadfile;
    }

    public void setUploadfile(MultipartFile uploadfile) {
        this.uploadfile = uploadfile;
    }
}
