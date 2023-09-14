package com.shaw.lts.handle.security.handler;

import com.shaw.lts.handle.demain.ApiOutput;
import com.shaw.lts.handle.utils.ServletUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * CustomerAccessDeniedHandler
 * 无权限访问
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2022/7/25 10:47
 **/
@Component
public class CustomerAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException e) throws IOException, ServletException {
        ServletUtils.write(response, ApiOutput.fail(600, "未登陆无法访问").json());
    }
}
