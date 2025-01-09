package com.jabibim.admin.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Academy {
    private String academyId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private String academyName;
    private String academyAddress;
    private String academyDetailAddr;
    private String academyPostalcode;
    private String academyOwner;
    private String academyContect;
    private String businessRegisNum;
    private LocalDateTime registeredAt;
}