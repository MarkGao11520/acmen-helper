package com.acmen.acmenhelper.common;

import javax.servlet.http.HttpServletRequest;

/**
 * @author gaowenfeng
 * @date 2018/5/18
 */
public class RequestHolder {


    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();


    public static void add(HttpServletRequest request) {
        requestHolder.set(request);
    }


    public static HttpServletRequest getCurrentRequest() {
        return requestHolder.get();
    }

}

