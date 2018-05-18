package com.acmen.acmenhelper.exception;

/**
 * Created by chenjiaqiang on 2018/5/18.
 */
public class GlobalException extends RuntimeException {
    private int code;
    private String msg;
    private Throwable e;

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

    public Throwable getE() {
        return e;
    }

    public void setE(Throwable e) {
        this.e = e;
    }

    public GlobalException(int code , String msg , Throwable e) {
        super(e);
        this.code = code;
        this.msg = msg;
        this.e = e;
    }
}
