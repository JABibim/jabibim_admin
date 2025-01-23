package com.jabibim.admin.domain;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Privacy {
    private int rnum;
    private String privacyTermId;

    @DateTimeFormat(pattern = "yyyy-MM-dd") // 날짜 형식 지정
    private LocalDate createdAt;

    private String privacyTermSubject;
    private String privacyTermName;
    private String privacyTermContent;

    @DateTimeFormat(pattern = "yyyy-MM-dd") // 날짜 형식 지정
    private LocalDate privacyTermExpirationDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd") // 날짜 형식 지정
    private LocalDate privacyTermEffectiveDate;

    private String privacyTermStatus;
    private String academyId;

    public String getPrivacyTermId() {
        return privacyTermId;
    }

    public void setPrivacyTermId(String privacyTermId) {
        this.privacyTermId = privacyTermId;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getPrivacyTermSubject() {
        return privacyTermSubject;
    }

    public void setPrivacyTermSubject(String privacyTermSubject) {
        this.privacyTermSubject = privacyTermSubject;
    }

    public String getPrivacyTermName() {
        return privacyTermName;
    }

    public void setPrivacyTermName(String privacyTermName) {
        this.privacyTermName = privacyTermName;
    }

    public String getPrivacyTermContent() {
        return privacyTermContent;
    }

    public void setPrivacyTermContent(String privacyTermContent) {
        this.privacyTermContent = privacyTermContent;
    }

    public LocalDate getPrivacyTermExpirationDate() {
        return privacyTermExpirationDate;
    }

    public void setPrivacyTermExpirationDate(LocalDate privacyTermExpirationDate) {
        this.privacyTermExpirationDate = privacyTermExpirationDate;
    }

    public LocalDate getPrivacyTermEffectiveDate() {
        return privacyTermEffectiveDate;
    }

    public void setPrivacyTermEffectiveDate(LocalDate privacyTermEffectiveDate) {
        this.privacyTermEffectiveDate = privacyTermEffectiveDate;
    }

    public String getPrivacyTermStatus() {
        return privacyTermStatus;
    }

    public void setPrivacyTermStatus(String privacyTermStatus) {
        this.privacyTermStatus = privacyTermStatus;
    }

    public String getAcademyId() {
        return academyId;
    }

    public int getRnum() {
        return rnum;
    }

    public void setRnum(int rnum) {
        this.rnum = rnum;
    }

    public void setAcademyId(String academyId) {
        this.academyId = academyId;
    }
}