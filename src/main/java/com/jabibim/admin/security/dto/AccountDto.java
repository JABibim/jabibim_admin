package com.jabibim.admin.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private String id;
    private String academyId;
    private String username;
    private String name;
    private String email;
    private String password;
    private String roles;
}
