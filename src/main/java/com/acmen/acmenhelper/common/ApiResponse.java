package com.acmen.acmenhelper.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * API格式封装
 * @author gaowenfeng
 */
@Data
public class ApiResponse {
    private int code;
    private String message;
    private Object data;
    private boolean more;

    public ApiResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ApiResponse() {
        this.code = Status.SUCCESS.getCode();
        this.message = Status.SUCCESS.getStandardMessage();
    }

    public static ApiResponse ofMessage(int code, String message) {
        return new ApiResponse(code, message, null);
    }

    public static ApiResponse ofSuccess(Object data) {
        return new ApiResponse(Status.SUCCESS.getCode(), Status.SUCCESS.getStandardMessage(), data);
    }

    public static ApiResponse ofStatus(Status status) {
        return new ApiResponse(status.getCode(), status.getStandardMessage(), null);
    }



    @Getter
    @AllArgsConstructor
    public enum Status {
        /** 成功 */
        SUCCESS(200, "OK"),
        /** 请求错误 */
        BAD_REQUEST(400, "Bad Request"),
        /** 资源不存在 */
        NOT_FOUND(404, "Not Found"),
        /** 服务器内部错误 */
        INTERNAL_SERVER_ERROR(500, "Unknown Internal Error"),
        /** 不合法的参数 */
        NOT_VALID_PARAM(40005, "Not valid Params"),
        /** 不支持 */
        NOT_SUPPORTED_OPERATION(40006, "Operation not supported"),
        /** 未登录 */
        NOT_LOGIN(50000, "Not Login");

        private int code;
        private String standardMessage;
    }
}
