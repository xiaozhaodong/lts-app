package com.shaw.lts.handle.controller;


import com.shaw.lts.handle.demain.ApiInput;
import com.shaw.lts.handle.demain.ApiOutput;
import com.shaw.lts.handle.handler.ApiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * BaseController
 * 基础的 Controller
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/6/16 15:23
 **/
public abstract class BaseController {

    @Autowired
    private ApiServiceImpl apiService;

    protected <I, O> ApiOutput<O> delegate(ApiInput<I> input) {
        return apiService.delegate(input);
    }
}
