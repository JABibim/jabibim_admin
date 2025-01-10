package com.jabibim.admin.domain;

import java.util.Date;

public class Student {  //학생 리스트와 갯수 구할때 쓰는거
    private String studentId;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private String studentName;
    private String studentEmail;
    private String studentPhone;
    private String studentPassword;
    private String studentAddress;
    private String verification;
    private String studentImgName;
    private String studentImgOrigin;
    private int adsAgreed;
    private String authRole;
    private String gradeId;
    private String academyId;
    private String academyName;
    private int enrollmentCount;
    private int paymentAmountSum;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getAcademyName() {
        return academyName;
    }

    public void setAcademyName(String academyName) {
        this.academyName = academyName;
    }

    public int getEnrollmentCount() {
        return enrollmentCount;
    }

    public void setEnrollmentCount(int enrollmentCount) {
        this.enrollmentCount = enrollmentCount;
    }

    public int getPaymentAmountSum() {
        return paymentAmountSum;
    }

    public void setPaymentAmountSum(int paymentAmountSum) {
        this.paymentAmountSum = paymentAmountSum;
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

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public void setStudentPhone(String studentPhone) {
        this.studentPhone = studentPhone;
    }

    public String getStudentPassword() {
        return studentPassword;
    }

    public void setStudentPassword(String studentPassword) {
        this.studentPassword = studentPassword;
    }

    public String getStudentAddress() {
        return studentAddress;
    }

    public void setStudentAddress(String studentAddress) {
        this.studentAddress = studentAddress;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public String getStudentImgName() {
        return studentImgName;
    }

    public void setStudentImgName(String studentImgName) {
        this.studentImgName = studentImgName;
    }

    public String getStudentImgOrigin() {
        return studentImgOrigin;
    }

    public void setStudentImgOrigin(String studentImgOrigin) {
        this.studentImgOrigin = studentImgOrigin;
    }

    public int getAdsAgreed() {
        return adsAgreed;
    }

    public void setAdsAgreed(int adsAgreed) {
        this.adsAgreed = adsAgreed;
    }

    public String getAuthRole() {
        return authRole;
    }

    public void setAuthRole(String authRole) {
        this.authRole = authRole;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getAcademyId() {
        return academyId;
    }

    public void setAcademyId(String academyId) {
        this.academyId = academyId;
    }
}
