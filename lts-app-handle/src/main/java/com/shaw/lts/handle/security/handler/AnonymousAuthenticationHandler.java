package com.shaw.lts.handle.security.handler;

import com.shaw.lts.handle.demain.ApiOutput;
import com.shaw.lts.handle.utils.ServletUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AnonymousAuthenticationHandler
 * 匿名用户无法访问
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2022/7/25 10:52
 **/
@Component
public class AnonymousAuthenticationHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException e) throws IOException {
        ServletUtils.write(response, ApiOutput.fail(403, "未登陆无法访问").json());
    }
}
