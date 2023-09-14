package com.shaw.lts.handle.handler;

import com.shaw.lts.handle.demain.ApiInput;
import com.shaw.lts.handle.demain.ApiOutput;
import com.shaw.lts.handle.demain.LoginUser;
import org.springframework.aop.framework.AopContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * AbstractApiHandler
 * api处理抽象
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/21 15:57
 **/
public abstract class AbstractApiHandler<I, O> {

    protected LoginUser getLoginUser() {
        // 从Security上下文中获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("获取用户信息失败");
        }
        if (authentication.getPrincipal() instanceof LoginUser) {
            return ((LoginUser) authentication.getPrincipal());
        } else {
            throw new RuntimeException("非登陆用户无法访问");
        }
    }

    public abstract ApiOutput<O> handle(ApiInput<I> input);


    @SuppressWarnings("unchecked")
    protected <T> T currentProxy(Class<T> interfaceType) {
        Object proxy = AopContext.currentProxy();
        if (interfaceType.isInstance(proxy)) {
            return (T) proxy;
        } else {
            throw new IllegalArgumentException("The proxy is not an instance of the interface");
        }
    }
}
