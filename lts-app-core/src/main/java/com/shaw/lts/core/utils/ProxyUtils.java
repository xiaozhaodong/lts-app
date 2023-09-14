package com.shaw.lts.core.utils;

import org.springframework.aop.framework.AopContext;

/**
 * ProxyUtils
 * 代理帮助 utils
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/24 14:24
 **/
public class ProxyUtils {

    @SuppressWarnings("unchecked")
    public static <T> T currentProxy(Class<T> interfaceType) {
        Object proxy = AopContext.currentProxy();
        if (interfaceType.isInstance(proxy)) {
            return (T) proxy;
        } else {
            throw new IllegalArgumentException("The proxy is not an instance of the interface");
        }
    }
}
