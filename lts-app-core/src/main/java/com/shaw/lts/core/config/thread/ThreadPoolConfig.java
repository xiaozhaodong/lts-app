package com.shaw.lts.core.config.thread;

import com.shaw.lts.core.utils.ThreadsUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * ThreadPoolConfig
 * 线程池配置
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/6/19 15:07
 **/
@Configuration
public class ThreadPoolConfig {

    @Bean(name = "scheduledExecutorService")
    public ScheduledExecutorService scheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(50,
            new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build()) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                ThreadsUtils.printException(r, t);
            }
        };
    }
}
