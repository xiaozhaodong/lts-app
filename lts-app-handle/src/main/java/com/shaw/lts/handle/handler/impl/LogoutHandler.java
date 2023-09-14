package com.shaw.lts.handle.handler.impl;

import com.shaw.lts.core.redis.RedisCacheService;
import com.shaw.lts.handle.demain.ApiInput;
import com.shaw.lts.handle.demain.ApiOutput;
import com.shaw.lts.handle.handler.AbstractApiHandler;
import com.shaw.lts.handle.utils.ServletUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * LogoutHandler
 * 用户退出登录 handler
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/26 13:57
 **/
@Component
public class LogoutHandler extends AbstractApiHandler<Void, Object> {

    private final RedisCacheService redisCacheService;

    public LogoutHandler(RedisCacheService redisCacheService) {
        this.redisCacheService = redisCacheService;
    }

    @Override
    public ApiOutput<Object> handle(ApiInput<Void> input) {
        String token = ServletUtils.getRequest().getHeader("token");
        String authTokenKey = "auth_token:" + token;
        Map<String, String> redisTokenMap = redisCacheService.getMap(authTokenKey);
        if (redisTokenMap != null && !redisTokenMap.isEmpty()) {
            redisCacheService.del(authTokenKey);
            String refreshToken = redisTokenMap.get("refreshToken");
            String refreshTokenKey = "refresh_token:" + refreshToken;
            String redisRefreshToken = redisCacheService.get(refreshTokenKey);
            if (StringUtils.isNotEmpty(redisRefreshToken)) {
                redisCacheService.del(authTokenKey);
            }
            return ApiOutput.ok().message("退出登录成功");
        } else {
            throw new RuntimeException("对应token已经失效");
        }
    }
}
