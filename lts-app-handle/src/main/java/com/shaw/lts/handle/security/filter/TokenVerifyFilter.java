package com.shaw.lts.handle.security.filter;

import com.shaw.lts.core.config.LtsProperties;
import com.shaw.lts.core.redis.RedisCacheService;
import com.shaw.lts.core.utils.IdGeneratorUtils;
import com.shaw.lts.handle.demain.ApiOutput;
import com.shaw.lts.handle.security.JwtService;
import com.shaw.lts.handle.security.LoginUserServiceImpl;
import com.shaw.lts.handle.utils.DateUtils;
import com.shaw.lts.handle.utils.NetworkUtils;
import com.shaw.lts.handle.utils.ServletUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * TokenVerifyFilter
 * token验证过滤器
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2022/7/18 16:32
 **/
@Component
public class TokenVerifyFilter extends OncePerRequestFilter {

    /**
     * 日志跟踪id名。
     */
    private static final String LOG_TRACE_ID = "traceId";

    private static final Logger logger = LoggerFactory.getLogger(TokenVerifyFilter.class);

    private final LtsProperties properties;

    private final RedisCacheService redisCacheService;

    private final JwtService jwtService;

    private final LoginUserServiceImpl loginUserService;

    public TokenVerifyFilter(LtsProperties properties, RedisCacheService redisCacheService, JwtService jwtService,
        LoginUserServiceImpl loginUserService) {
        this.properties = properties;
        this.redisCacheService = redisCacheService;
        this.jwtService = jwtService;
        this.loginUserService = loginUserService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        MDC.put("request_start_time", String.valueOf(startTime));
        MDC.put("handle_start_time", DateUtils.dateToString(new Date(startTime), "yyyy-MM-dd HH:mm:ss"));
        MDC.put(LOG_TRACE_ID, "traceId:" + IdGeneratorUtils.uuid() + ",");
        String url = request.getRequestURI();
        String requestIp = NetworkUtils.getIpAddress(request);
        MDC.put("request_ip", requestIp);
        MDC.put("request_url", url);
        logger.info("请求ip:{},url:{}", requestIp, url);
        // 不是登录请求需要验证token
        if (!ServletUtils.matchUrl(request, properties.getAllowUrl())) {
            // 从请求头中获取token
            String token = request.getHeader("token");
            if (StringUtils.isEmpty(token)) {
                logger.error("请求头token参数为空");
                ServletUtils.write(response, ApiOutput.fail().message("请求头token参数为空").json());
                return;
            }
            Map<String, String> authTokenMap = redisCacheService.getMap("auth_token:" + token);
            if (authTokenMap == null || authTokenMap.isEmpty()) {
                logger.error("当前token已过期");
                ServletUtils.write(response, ApiOutput.fail(202, "当前token已过期").json());
                return;
            }
            String refreshToken = authTokenMap.get("refreshToken");
            if (StringUtils.isEmpty(refreshToken)) {
                logger.error("对应刷新token为空,请重新登录");
                ServletUtils.write(response, ApiOutput.fail(203, "刷新token为空,请重新登录").json());
                return;
            }
            String refreshTokenKey = "refresh_token:" + refreshToken;
            String redisRefreshToken = redisCacheService.get(refreshTokenKey);
            if (StringUtils.isEmpty(redisRefreshToken)) {
                logger.error("对应刷新token不存在");
                ServletUtils.write(response, ApiOutput.fail(203, "刷新token已过期,请重新登录").json());
                return;
            }
            if (!refreshToken.equals(redisRefreshToken)) {
                logger.error("对应刷新token非法");
                ServletUtils.write(response, ApiOutput.fail(203, "刷新token非法,请重新登录").json());
                return;
            }
            if (jwtService.isTokenExpired(redisRefreshToken)) {
                logger.error("对应刷新token已失效");
                ServletUtils.write(response, ApiOutput.fail(203, "刷新token已失效,请重新登录").json());
                return;
            }
            String redisToken = authTokenMap.get("token");
            if (StringUtils.isEmpty(redisToken)) {
                logger.error("当前token不存在");
                ServletUtils.write(response, ApiOutput.fail(202, "当前token已过期").json());
                return;
            }
            if (!token.equals(redisToken)) {
                logger.error("当前token非法");
                ServletUtils.write(response, ApiOutput.fail(202, "当前token非法").json());
                return;
            }
            if (jwtService.isTokenExpired(redisToken)) {
                logger.error("当前token已失效");
                ServletUtils.write(response, ApiOutput.fail(202, "当前token已失效").json());
                return;
            }
            // 如果存在token，则从token中解析出用户名
            String username = jwtService.getUsernameFromToken(token);
            if (StringUtils.isEmpty(username)) {
                logger.error("当前token解析失败");
                ServletUtils.write(response, ApiOutput.fail(202, "当前token解析失败").json());
                return;
            }
            UserDetails userDetails = loginUserService.loadUserByUsername(username);
            if (userDetails == null) {
                logger.error("当前token验证失败");
                ServletUtils.write(response, ApiOutput.fail(202, "当前token验证失败").json());
                return;
            }
            // 创建身份验证对象
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // 设置到Spring Security上下文
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        doFilter(request, response, filterChain);
        MDC.clear();
    }
}
