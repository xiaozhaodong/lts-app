package com.shaw.lts.handle.demain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shaw.lts.core.utils.JacksonUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * Response
 * web响应信息
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2022/7/18 11:37
 **/
@Schema(title = "ApiOutput", description = "请求响应信息")
public class ApiOutput<T> implements Serializable {

    private static final long serialVersionUID = 2250597900338079061L;

    /**
     * 成功
     */
    public static final Integer SUCCESS = 200;

    /**
     * 失败状态码
     */
    public static final Integer FAIL = 500;

    /**
     * 未登录状态码
     */
    public static final int NO_LOGIN = 600;

    /**
     * 没有权限状态码
     */
    public static final int NO_AUTH = 700;

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

//    @ArraySchema(schema = @Schema(implementation =Object.class))
    @Schema(description = "响应实体对象")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static <T> ApiOutput<T> ok() {
        ApiOutput<T> apiOutput = new ApiOutput<>();
        apiOutput.setCode(SUCCESS);
        apiOutput.setMessage("请求处理成功");
        apiOutput.setSuccess(true);
        return apiOutput;
    }

    public static <T> ApiOutput<T> ok(T data) {
        ApiOutput<T> apiOutput = new ApiOutput<>();
        apiOutput.setCode(SUCCESS);
        apiOutput.setMessage("请求处理成功");
        apiOutput.setSuccess(true);
        apiOutput.setData(data);
        return apiOutput;
    }

    public static <T> ApiOutput<T> fail() {
        ApiOutput<T> apiOutput = new ApiOutput<>();
        apiOutput.setCode(FAIL);
        apiOutput.setMessage("请求处理失败");
        apiOutput.setSuccess(false);
        return apiOutput;
    }

    public static ApiOutput<?> fail(Integer code, String message) {
        ApiOutput<?> apiOutput = new ApiOutput<>();
        apiOutput.setCode(code);
        apiOutput.setMessage(message);
        apiOutput.setSuccess(false);
        return apiOutput;
    }

    public static <T> ApiOutput<T> fail(T data) {
        ApiOutput<T> apiOutput = new ApiOutput<>();
        apiOutput.setCode(FAIL);
        apiOutput.setMessage("请求处理失败");
        apiOutput.setSuccess(false);
        apiOutput.setData(data);
        return apiOutput;
    }

    /**
     * 设置状态码
     *
     * @param code 状态码
     * @return {@link ApiOutput}
     */
    public ApiOutput<T> code(Integer code) {
        this.setCode(code);

        return this;
    }

    /**
     * 设置返回消息
     *
     * @param message 返回消息
     * @return {@link ApiOutput}
     */
    public ApiOutput<T> message(String message) {
        this.setMessage(message);
        return this;
    }

    public String json() {
        return JacksonUtils.toJson(this);
    }

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

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
