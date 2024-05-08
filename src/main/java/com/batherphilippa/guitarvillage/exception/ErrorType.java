package com.batherphilippa.guitarvillage.exception;

public enum ErrorType {

    NOT_FOUND(404, "NOT FOUND");

    private int code;
    private String msg;
    ErrorType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
