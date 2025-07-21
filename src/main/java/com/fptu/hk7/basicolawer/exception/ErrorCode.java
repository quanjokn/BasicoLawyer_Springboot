package com.fptu.hk7.basicolawer.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    INVALID_KEY(1001, "Uncategorized error"),
    INVALID_CATEGORY(1001, "Can not find category"),
    USER_NAME_EXISTED(1002, "User existed"),
    EMAIL_EXISTED(1002, "User existed"),
    USER_NOT_FOUND(1000, "User not found"),
    USERNAME_INVALID(1003, "Username must be at least 3 characters"),
    INVALID_PASSWORD(1004, "Password must be at least 8 characters"),
    LOGIN_FAIL(400, "Incorrect username or password"),
    INVALID_EMAIL(1005, "Email is not existed or valid"),
    ERROR_OTP(1006, "OTP is not similar or expired"),
    IO_EXCEPTION(1008, "Error input stream with unexpected data type"),
    UNABLE_TO_CHANGE_PASSWORD(1007, "Can not change password"),
    FAIL_VALIDATION_DATA(1003, "Error format!"),
    INCORRECT_PASSWORD(1009, "Incorrect password"),
    INVALID_POST(1001, "Can not find post"),
    INVALID_STAFF_PROFILE(1001, "Staff profile not find"),
    ;

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code; // Assigning code based on ordinal
        this.message = message;
    }

}
