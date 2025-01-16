package com.jabibim.admin.dto.common;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private boolean isSuccess;
    private T data;
    private String message;

    public ApiResponse(boolean isSuccess, T data, String message) {
        this.isSuccess = isSuccess;
        this.data = data;
        this.message = message;
    }
}
