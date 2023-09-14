package com.shaw.lts.handle.demain;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * BaseRespDoc
 * 基础的 显示内容
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/6/21 14:37
 **/
public class BaseRespDoc {

    /**
     * 是否成功
     */
    @Schema(description = "是否成功")
    private boolean success;

    /**
     * 响应码
     */
    @Schema(description = "响应码")
    private Integer code;

    /**
     * 响应描述
     */
    @Schema(description = "响应描述")
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
