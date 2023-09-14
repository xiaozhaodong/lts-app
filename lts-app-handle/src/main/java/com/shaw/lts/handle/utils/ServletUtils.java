package com.shaw.lts.handle.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * ServletUtils
 * 响应写出帮助
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2022/7/18 16:45
 **/
public class ServletUtils {

    private static final Logger log = LoggerFactory.getLogger(ServletUtils.class);

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取response
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    /**
     * 获取session
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    public static boolean matchUrl(List<String> urls) {
        HttpServletRequest request = ServletUtils.getRequest();
        return matchUrl(request, urls);
    }

    public static boolean matchUrl(HttpServletRequest request, List<String> urls) {
        if (CollectionUtils.isNotEmpty(urls)) {
            for (String url : urls) {
                AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(url);
                RequestMatcher.MatchResult result = requestMatcher.matcher(request);
                if (result.isMatch()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void write(HttpServletResponse response, String context) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(context.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
