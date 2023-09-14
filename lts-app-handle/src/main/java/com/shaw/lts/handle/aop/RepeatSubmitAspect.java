package com.shaw.lts.handle.aop;

import com.shaw.lts.core.redis.RedisCacheService;
import com.shaw.lts.core.utils.JacksonUtils;
import com.shaw.lts.handle.demain.ApiInput;
import com.shaw.lts.handle.demain.ApiOutput;
import com.shaw.lts.handle.utils.ServletUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * RepeatSubmitAspect
 * 重复提交处理Aspect
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/6/19 15:31
 **/
@Aspect
@Component
public class RepeatSubmitAspect {

    private final String REPEAT_PARAMS = "repeatParams";

    private final String REPEAT_TIME = "repeatTime";

    private final RedisCacheService redisCacheService;

    public RepeatSubmitAspect(RedisCacheService redisCacheService) {
        this.redisCacheService = redisCacheService;
    }

    @Pointcut("@annotation(com.shaw.lts.handle.annotation.RepeatSubmit)")
    public void pointcut() {

    }

    @Around(value = "pointcut()")
    public Object doConcurrentOperation(@NonNull ProceedingJoinPoint pjp) throws Throwable {
        ApiInput<?> apiInput = ((ApiInput<?>) pjp.getArgs()[0]);
        if (apiInput.getData() == null) {
            throw new RuntimeException("输入参数为空");
        }
        String inputJson = JacksonUtils.toJson(apiInput.getData());
        Map<String, Object> nowDataJson = new HashMap<>();
        nowDataJson.put(REPEAT_PARAMS, inputJson);
        nowDataJson.put(REPEAT_TIME, System.currentTimeMillis());
        String url = MDC.get("request_url");
        String submitKey = ServletUtils.getRequest().getHeader("token");
        if (StringUtils.isNotEmpty(submitKey)) {
            submitKey = "repeat_submit:" + submitKey;
        } else {
            submitKey = "repeat_submit:" + url;
        }
        Map<String, String> dataMap = redisCacheService.getMap(submitKey);
        if (dataMap != null && !dataMap.isEmpty()) {
            if (dataMap.containsKey(url)) {
                Map<String, Object> oldDataJson = JacksonUtils.toMap(dataMap.get(url));
                assert oldDataJson != null;
                if (compareParams(nowDataJson, oldDataJson) && compareTime(nowDataJson, oldDataJson)) {
                    return ApiOutput.fail(600, "不允许重复提交");
                }
            }
        }
        Map<String, String> cacheMap = new HashMap<>();
        cacheMap.put(url, JacksonUtils.toJson(nowDataJson));
        redisCacheService.setMap(submitKey, cacheMap, 10);
        return pjp.proceed();
    }

    /**
     * 判断参数是否相同
     */
    private boolean compareParams(Map<String, Object> nowDataJson, Map<String, Object> oldDataJson) {
        String nowParams = (String) nowDataJson.get(REPEAT_PARAMS);
        String preParams = (String) oldDataJson.get(REPEAT_PARAMS);
        return nowParams.equals(preParams);
    }

    /**
     * 判断两次间隔时间
     */
    private boolean compareTime(Map<String, Object> nowDataJson, Map<String, Object> oldDataJson) {
        long time1 = (long) nowDataJson.get(REPEAT_TIME);
        long time2 = (long) oldDataJson.get(REPEAT_TIME);
        return (time1 - time2) < (10 * 1000);
    }
}
