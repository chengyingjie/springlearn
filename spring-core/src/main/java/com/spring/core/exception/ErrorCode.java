package com.spring.core.exception;

/**
 * 错误码
 */
public enum ErrorCode implements IErrorCode {

    SYSTEM_EXCEPTION(0000_00001, "系统异常"),
    KEY_CONFLICT(0000_00002, "spring bean冲突"),
    ILLEGAL_PARAM(0000_00003, "参数不合法"),
    ;


    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
