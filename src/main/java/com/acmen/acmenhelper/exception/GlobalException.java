package com.acmen.acmenhelper.exception;

/**
 * Created by chenjiaqiang on 2018/5/18.
 */
public class GlobalException extends Exception {
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public GlobalException(int code , String msg) {
        super(msg);
        this.code = code;
    }
}
