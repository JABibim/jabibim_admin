package com.jabibim.admin.domain;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Term {
    private int rnum;
    private String serviceTermId;

    @DateTimeFormat(pattern = "yyyy-MM-dd") // 날짜 형식 지정
    private LocalDate createdAt;

    private String serviceTermSubject;
    private String serviceTermName;
    private String serviceTermContent;

    @DateTimeFormat(pattern = "yyyy-MM-dd") // 날짜 형식 지정
    private LocalDate serviceTermExpirationDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd") // 날짜 형식 지정
    private LocalDate serviceTermEffectiveDate;

    private String serviceTermStatus;
    private String academyId;

    public String getServiceTermId() {
        return serviceTermId;
    }

    public void setServiceTermId(String serviceTermId) {
        this.serviceTermId = serviceTermId;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getServiceTermSubject() {
        return serviceTermSubject;
    }

    public void setServiceTermSubject(String serviceTermSubject) {
        this.serviceTermSubject = serviceTermSubject;
    }

    public String getServiceTermName() {
        return serviceTermName;
    }

    public void setServiceTermName(String serviceTermName) {
        this.serviceTermName = serviceTermName;
    }

    public String getServiceTermContent() {
        return serviceTermContent;
    }

    public void setServiceTermContent(String serviceTermContent) {
        this.serviceTermContent = serviceTermContent;
    }

    public LocalDate getServiceTermExpirationDate() {
        return serviceTermExpirationDate;
    }

    public void setServiceTermExpirationDate(LocalDate serviceTermExpirationDate) {
        this.serviceTermExpirationDate = serviceTermExpirationDate;
    }

    public LocalDate getServiceTermEffectiveDate() {
        return serviceTermEffectiveDate;
    }

    public void setServiceTermEffectiveDate(LocalDate serviceTermEffectiveDate) {
        this.serviceTermEffectiveDate = serviceTermEffectiveDate;
    }

    public String getServiceTermStatus() {
        return serviceTermStatus;
    }

    public void setServiceTermStatus(String serviceTermStatus) {
        this.serviceTermStatus = serviceTermStatus;
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
}