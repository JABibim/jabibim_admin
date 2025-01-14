package com.jabibim.admin.domain;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class Qna {
    private int rnum;
    private String qnaId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private int qnaReRef;
    private int qnaReLev;
    private int qnaReSeq;
    private String qnaSubject;
    private String qnaPassword;
    private String qnaContent;
    private int qnaReadCount;
    private int qnaExposureStat;
    private int qnaAnswerStatus;
    private String qnaFileName;
    private String qnaFileOrigin;
    private String academyId;
    private String teacherId;
    private String teacherName;
    private String studentId;
    private String studentName;
    private String courseId;
    private String courseName;
    private String classId;
    private String className;
    private MultipartFile uploadfile;

    public int getRnum() {
        return rnum;
    }

    public void setRnum(int rnum) {
        this.rnum = rnum;
    }

    public String getQnaId() {
        return qnaId;
    }

    public void setQnaId(String qnaId) {
        this.qnaId = qnaId;
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

    public int getQnaReRef() {
        return qnaReRef;
    }

    public void setQnaReRef(int qnaReRef) {
        this.qnaReRef = qnaReRef;
    }

    public int getQnaReLev() {
        return qnaReLev;
    }

    public void setQnaReLev(int qnaReLev) {
        this.qnaReLev = qnaReLev;
    }

    public int getQnaReSeq() {
        return qnaReSeq;
    }

    public void setQnaReSeq(int qnaReSeq) {
        this.qnaReSeq = qnaReSeq;
    }

    public String getQnaSubject() {
        return qnaSubject;
    }

    public void setQnaSubject(String qnaSubject) {
        this.qnaSubject = qnaSubject;
    }

    public String getQnaPassword() {
        return qnaPassword;
    }

    public void setQnaPassword(String qnaPassword) {
        this.qnaPassword = qnaPassword;
    }

    public String getQnaContent() {
        return qnaContent;
    }

    public void setQnaContent(String qnaContent) {
        this.qnaContent = qnaContent;
    }

    public int getQnaReadCount() {
        return qnaReadCount;
    }

    public void setQnaReadCount(int qnaReadCount) {
        this.qnaReadCount = qnaReadCount;
    }

    public int getQnaExposureStat() {
        return qnaExposureStat;
    }

    public void setQnaExposureStat(int qnaExposureStat) {
        this.qnaExposureStat = qnaExposureStat;
    }

    public int getQnaAnswerStatus() {
        return qnaAnswerStatus;
    }

    public void setQnaAnswerStatus(int qnaAnswerStatus) {
        this.qnaAnswerStatus = qnaAnswerStatus;
    }

    public String getQnaFileName() {
        return qnaFileName;
    }

    public void setQnaFileName(String qnaFileName) {
        this.qnaFileName = qnaFileName;
    }

    public String getQnaFileOrigin() {
        return qnaFileOrigin;
    }

    public void setQnaFileOrigin(String qnaFileOrigin) {
        this.qnaFileOrigin = qnaFileOrigin;
    }

    public String getAcademyId() {
        return academyId;
    }

    public void setAcademyId(String academyId) {
        this.academyId = academyId;
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

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public MultipartFile getUploadfile() {
        return uploadfile;
    }

    public void setUploadfile(MultipartFile uploadfile) {
        this.uploadfile = uploadfile;
    }
}
