package com.fptu.hk7.basicolawer.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean status;
    private String message;
    private int code;
    private T result;
}
