package com.quest.etna.utils;

@SuppressWarnings("serial")
public class CustomException extends RuntimeException {

    private int code;

    public CustomException(int code) {
        super();
        this.code = code;
    }

    public CustomException(String message, int code) {
        super(message);
        this.code = code;
    }

    public CustomException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    public CustomException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}