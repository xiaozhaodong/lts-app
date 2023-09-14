package com.shaw.lts.core.config.thread;

import com.shaw.lts.core.utils.ThreadsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;

/**
 * ThreadPoolDestroy
 * 线程销毁
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/7 14:43
 **/
@Component
public class ThreadPoolDestroy implements DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ScheduledExecutorService scheduledExecutorService;

    public ThreadPoolDestroy(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
    }

    @Override
    public void destroy() {
        ThreadsUtils.shutdownAndAwaitTermination(scheduledExecutorService);
        logger.info("ScheduledExecutorService Destroyed");
    }
}
