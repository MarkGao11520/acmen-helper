package com.acmen.acmenhelper.common;

import lombok.Data;
import lombok.ToString;

/**
 * Datatables响应结构
 * @author gaowenfeng
 */
@Data
@ToString
public class ApiDataTableResponse extends ApiResponse {
    private int draw;
    private long recordsTotal;
    private long recordsFiltered;

    public ApiDataTableResponse(Status status) {
        this(status.getCode(), status.getStandardMessage(), null);
    }

    public ApiDataTableResponse(int code, String message, Object data) {
        super(code, message, data);
    }

}
