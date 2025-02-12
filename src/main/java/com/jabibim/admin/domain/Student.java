package com.jabibim.admin.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Student implements Serializable {  //학생 리스트와 갯수 구할때 쓰는거
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



}
