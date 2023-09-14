package com.shaw.lts.handle.security.handler;

import com.shaw.lts.handle.demain.ApiOutput;
import com.shaw.lts.handle.utils.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * LoginFailHandler
 * 登录失败处理
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2022/7/18 11:30
 **/
@Component
public class LoginFailHandler implements AuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(LoginFailHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException e) throws IOException {
        String message = "登录失败";
        Integer code = ApiOutput.FAIL;

        if (e instanceof BadCredentialsException) {
            message = e.getMessage();
        } else if (e instanceof LockedException) {
            message = "该用户账号已被锁定";
        } else if (e instanceof AccountExpiredException) {
            message = "该用户账号已过期";
        } else if (e instanceof CredentialsExpiredException) {
            message = "该用户凭证已过期";
        } else if (e instanceof DisabledException) {
            message = "该用户账户已禁用";
        } else if (e instanceof UsernameNotFoundException || e instanceof AuthenticationServiceException) {
            message = e.getMessage();
        }
        logger.info(message);

        ServletUtils.write(response, ApiOutput.fail(code, message).json());
    }
}
