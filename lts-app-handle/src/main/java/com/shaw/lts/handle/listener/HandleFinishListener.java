package com.shaw.lts.handle.listener;

import com.shaw.lts.adapter.service.SysHandleLogService;
import com.shaw.lts.handle.listener.event.HandleFinishEvent;
import com.shaw.lts.persist.model.SysHandleLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * HandleFinishListener
 * 处理完成监听
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2022/8/4 09:41
 **/
@Component
public class HandleFinishListener implements ApplicationListener<HandleFinishEvent> {

    private static final Logger logger = LoggerFactory.getLogger(HandleFinishListener.class);

    private final SysHandleLogService handleLogService;

    private final ScheduledExecutorService scheduledExecutorService;

    public HandleFinishListener(SysHandleLogService handleLogService, ScheduledExecutorService scheduledExecutorService) {
        this.handleLogService = handleLogService;
        this.scheduledExecutorService = scheduledExecutorService;
    }

    @Override
    public void onApplicationEvent(@NonNull HandleFinishEvent event) {
        try {
            SysHandleLog handleLog = (SysHandleLog) event.getSource();
            scheduledExecutorService.schedule(() -> {
                MDC.put("traceId", "traceId:" + handleLog.getTraceId() + ",");
                handleLogService.save(handleLog);
                logger.info("新增操作日志成功");
                MDC.remove("traceId");
            }, 10, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("新增操作日志异常:" + e.getMessage());
        }
    }

}
