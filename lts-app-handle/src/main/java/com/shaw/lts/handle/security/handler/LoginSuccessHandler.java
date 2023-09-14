package com.shaw.lts.handle.security.handler;

import com.shaw.lts.core.config.LtsProperties;
import com.shaw.lts.core.redis.RedisCacheService;
import com.shaw.lts.core.utils.JacksonUtils;
import com.shaw.lts.handle.constant.LtsConst;
import com.shaw.lts.handle.demain.ApiOutput;
import com.shaw.lts.handle.demain.LoginUser;
import com.shaw.lts.handle.demain.vo.LoginVo;
import com.shaw.lts.handle.security.JwtService;
import com.shaw.lts.handle.utils.DateUtils;
import com.shaw.lts.handle.utils.ServletUtils;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * LoginSuccessHandler
 * 登录成功处理
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2022/7/18 11:12
 **/
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(LoginSuccessHandler.class);

    private final JwtService jwtService;

    private final LtsProperties properties;

    private final RedisCacheService redisCacheService;

    public LoginSuccessHandler(JwtService jwtService, LtsProperties properties, RedisCacheService redisCacheService) {
        this.jwtService = jwtService;
        this.properties = properties;
        this.redisCacheService = redisCacheService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        LoginUser loginUser = ((LoginUser) authentication.getPrincipal());
//        String loginUserJson = JacksonUtils.toJson(loginUser);
        String token = jwtService.generateToken(loginUser);
        String authTokenKey = "auth_token:" + token;
        long tokenExpireTime = Jwts.parser().setSigningKey(properties.getJwtSecret())
            .parseClaimsJws(token.replace("jwt_", "")).getBody().getExpiration().getTime();
        long secondsToken = properties.getJwtExpiredTime() / 1000L;
        Map<String, String> authTokenMap = new HashMap<>();
        authTokenMap.put("token", token);
//        authTokenMap.put("userInfo", loginUserJson);
        String refreshToken = jwtService.generateRefreshToken(loginUser);
        authTokenMap.put("refreshToken", refreshToken);
        redisCacheService.setMap(authTokenKey, authTokenMap, (int) secondsToken);
        String refreshTokenKey = "refresh_token:" + refreshToken;
        long refreshTokenExpireTime = Jwts.parser().setSigningKey(properties.getJwtSecret())
            .parseClaimsJws(refreshToken.replace("jwt_", "")).getBody().getExpiration().getTime();
        long secondsRefreshToken = properties.getRefreshExpiredTime() / 1000L;
        redisCacheService.set(refreshTokenKey, refreshToken, (int) secondsRefreshToken);
        LoginVo loginVo = installLoginVo(loginUser, token, tokenExpireTime, refreshToken, refreshTokenExpireTime);
        logger.info("用户:{},登录成功,返回:{}", loginUser.getLoginName(), JacksonUtils.toJson(loginVo));
        ServletUtils.write(response, ApiOutput.ok(loginVo).message("登陆成功").json());
    }

    private LoginVo installLoginVo(LoginUser loginUser, String token, long tokenExpireTime, String refreshToken,
        long refreshTokenExpireTime) {
        LoginVo loginVo = new LoginVo();
        loginVo.setUserId(loginUser.getUserId());
        loginVo.setUserName(
            StringUtils.isNotEmpty(loginUser.getUserName()) ? loginUser.getUserName() : loginUser.getLoginName());
        loginVo.setBlnVip(LtsConst.Y.equals(loginUser.getBlnVip()));
        if (loginVo.isBlnVip()) {
            loginVo.setVipExpireTime(DateUtils.dateToString(loginUser.getVipExpireTime(), "yyyy-MM-dd HH:mm:ss"));
        } else {
            loginVo.setVipExpireTime("");
        }
        loginVo.setToken(token);
        loginVo.setTokenExpire(tokenExpireTime);
        loginVo.setRefreshToken(refreshToken);
        loginVo.setRefreshTokenExpire(refreshTokenExpireTime);
        loginVo.setBlnOnline(LtsConst.Y.equals(loginUser.getBlnOnline()));
        return loginVo;
    }
}
