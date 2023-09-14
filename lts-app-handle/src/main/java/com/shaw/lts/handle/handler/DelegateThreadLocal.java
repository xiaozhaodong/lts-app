package com.shaw.lts.handle.handler;

/**
 * DelegateThreadLocal
 * 委派ThreadLocal
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/6/16 17:54
 **/
public class DelegateThreadLocal {

    private final static ThreadLocal<Class<? extends AbstractApiHandler<?, ?>>> HANDLER_THREAD_LOCAL = new ThreadLocal<>();

    public static Class<? extends AbstractApiHandler<?, ?>> getHandlerClass() {
        return HANDLER_THREAD_LOCAL.get();
    }

    public static void putHandlerClass(Class<? extends AbstractApiHandler<?, ?>> handlerClass) {
        HANDLER_THREAD_LOCAL.set(handlerClass);
    }

    public static void remove() {
        HANDLER_THREAD_LOCAL.remove();
    }
}
