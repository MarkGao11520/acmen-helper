package com.acmen.acmenhelper.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * service层统一返回结果封装
 * @author gaowenfeng
 * @date 2018/5/16
 */
@Data
@AllArgsConstructor
public class ServiceResult<T> {

    private boolean isSuccess;

    private String msg;

    private T result;

    public static  <T> ServiceResult<T> of(T obj){
        return new ServiceResult<T>(true,null,obj);
    }

    public static <T> ServiceResult<T> success() {
        return new ServiceResult<>(true,null,null);
    }

    public static <T> ServiceResult<T> notFound() {
        return new ServiceResult<>(false, Message.NOT_FOUND.getValue(),null);
    }

    @AllArgsConstructor
    @Getter
    public enum Message {
        /** 未找到资源 */
        NOT_FOUND("Not Found Resource!");

        private String value;
    }


}
