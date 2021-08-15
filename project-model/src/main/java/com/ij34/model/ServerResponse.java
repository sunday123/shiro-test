package com.ij34.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerResponse {
    /**
     * 状态码，0表示成功过，1表示处理中，-1表示失败
     */
    private Integer code;
    /**
     * 业务数据
     */
    private Object data;
    /**
     * 信息描述
     */
    private String msg;


    /**
     * 成功，不用返回数据
     *
     * @return
     */
    public static ServerResponse buildSuccess() {
        return new ServerResponse(0, null, null);
    }

    /**
     * 成功，返回数据
     *
     * @param data 返回数据
     * @return
     */
    public static ServerResponse buildSuccess(Object data) {
        return new ServerResponse(0, data, null);
    }

    /**
     * 成功，返回数据
     *
     * @param code 状态码
     * @param data 返回数据
     * @return
     */
    public static ServerResponse buildSuccess(int code, Object data) {
        return new ServerResponse(code, data, null);
    }

    /**
     * 失败，返回信息
     *
     * @param msg 返回信息
     * @return
     */
    public static ServerResponse buildError(String msg) {
        return new ServerResponse(-1, null, msg);
    }

    /**
     * 失败，返回信息和状态码
     *
     * @param code 状态码
     * @param msg  返回信息
     * @return
     */
    public static ServerResponse buildError(Integer code, String msg) {
        return new ServerResponse(code, null, msg);
    }


}
