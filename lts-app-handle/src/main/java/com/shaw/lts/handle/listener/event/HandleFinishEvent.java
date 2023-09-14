package com.shaw.lts.handle.listener.event;

import com.shaw.lts.persist.model.SysHandleLog;
import org.springframework.context.ApplicationEvent;

/**
 * HandleFinishEvent
 * 处理完成事件
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2022/8/4 09:42
 **/
public class HandleFinishEvent extends ApplicationEvent {

    private static final long serialVersionUID = -5517481818450012264L;

    public HandleFinishEvent(SysHandleLog sysHandleLog) {
        super(sysHandleLog);
    }
}
