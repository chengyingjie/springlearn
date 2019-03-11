package com.spring.core.exception;

/**
 * 自定义异常
 */
public class SpringLearnException extends RuntimeException {

    private final int errorCode;

    public SpringLearnException(int errorCode) {
        this.errorCode = errorCode;
    }

    public SpringLearnException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public SpringLearnException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public SpringLearnException(int errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public SpringLearnException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.getCode();
    }

    public SpringLearnException(IErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode.getCode();
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}
