package com.shaw.lts.handle.exception;


import com.shaw.lts.handle.demain.ApiOutput;

/**
 * 业务异常
 *
 * @author morgan
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -5598862461972116952L;

    /**
     * 异常错误码
     */
    private Integer code;

    /**
     * 异常错误消息
     */
    private String message;

    public BusinessException(String message) {
        this.code = ApiOutput.FAIL;
        this.message = message;
    }

    public BusinessException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
