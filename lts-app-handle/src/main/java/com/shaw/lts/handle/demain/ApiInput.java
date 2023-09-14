package com.shaw.lts.handle.demain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * MssInput
 * 输入参数
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2022/7/25 12:45
 **/
public class ApiInput<I> {


    /**
     * 处理器需要参数
     */
    private I data;

    /**
     * 请求
     */
    private HttpServletRequest request;

    /**
     * 响应
     */
    private HttpServletResponse response;

    public I getData() {
        return data;
    }

    public void setData(I data) {
        this.data = data;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public static <I> ApiInput<I> input() {
        return new ApiInput<>();
    }

    public static <I> ApiInput<I> input(I data) {
        ApiInput<I> apiInput = new ApiInput<>();
        apiInput.setData(data);
        return apiInput;
    }

    public static <I> ApiInput<I> input(I data, HttpServletRequest request, HttpServletResponse response) {
        ApiInput<I> apiInput = new ApiInput<>();
        apiInput.setData(data);
        apiInput.setRequest(request);
        apiInput.setResponse(response);
        return apiInput;
    }

    public static <I> ApiInput<I> input(HttpServletRequest request, HttpServletResponse response) {
        ApiInput<I> apiInput = new ApiInput<>();
        apiInput.setRequest(request);
        apiInput.setResponse(response);
        return apiInput;
    }

    public static <I> ApiInput<I> input(HttpServletRequest request) {
        ApiInput<I> apiInput = new ApiInput<>();
        apiInput.setRequest(request);
        return apiInput;
    }
}
