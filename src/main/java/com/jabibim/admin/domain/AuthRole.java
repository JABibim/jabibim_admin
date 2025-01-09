package com.jabibim.admin.domain;

import lombok.Getter;

@Getter
public enum AuthRole {
    ROLE_ADMIN(1, "ROLE_ADMIN"),
    ROLE_MANAGER(2, "ROLE_MANAGER"),
    ROLE_LECTURER(3, "ROLE_LECTURER"),
    ROLE_UNKNOWN(99, "ROLE_UNKNOWN");

    final private int priority; // 우선 순위
    final private String name; // 권한 이름

    AuthRole(int priority, String name) {
        this.priority = priority;
        this.name = name;
    }

    public static AuthRole of(int priority) {
        for (AuthRole authRole : AuthRole.values()) {
            if (authRole.priority == priority) {
                return authRole;
            }
        }
        return ROLE_UNKNOWN;
    }
}
