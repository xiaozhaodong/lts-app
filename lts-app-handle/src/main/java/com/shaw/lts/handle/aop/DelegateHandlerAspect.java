package com.shaw.lts.handle.aop;

import com.shaw.lts.core.utils.JacksonUtils;
import com.shaw.lts.handle.annotation.DelegateHandler;
import com.shaw.lts.handle.demain.ApiOutput;
import com.shaw.lts.handle.demain.LoginUser;
import com.shaw.lts.handle.handler.AbstractApiHandler;
import com.shaw.lts.handle.handler.DelegateThreadLocal;
import com.shaw.lts.handle.listener.event.HandleFinishEvent;
import com.shaw.lts.handle.utils.DateUtils;
import com.shaw.lts.handle.utils.ServletUtils;
import com.shaw.lts.persist.model.SysHandleLog;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * DelegateHandlerAspect
 * 委派处理器Aspect
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/6/16 17:46
 **/
@Aspect
@Component
public class DelegateHandlerAspect {

    private static final Logger logger = LoggerFactory.getLogger(DelegateHandlerAspect.class);

    private final ApplicationContext applicationContext;

    public DelegateHandlerAspect(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Pointcut("@annotation(com.shaw.lts.handle.annotation.DelegateHandler)")
    public void pointcut() {

    }

    @Around(value = "pointcut()")
    public Object doConcurrentOperation(@NonNull ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String className = pjp.getTarget().getClass().getName();
        String methodName = methodSignature.getName();
        DelegateHandler delegateHandler = methodSignature.getMethod().getAnnotation(DelegateHandler.class);
        Class<? extends AbstractApiHandler<?, ?>> aClass = delegateHandler.handler();
        DelegateThreadLocal.putHandlerClass(aClass);
        SysHandleLog handleLog = new SysHandleLog();
        fillRequestInfo(className, methodName, delegateHandler, handleLog);
        fillUserInfo(handleLog);
        String requestParam = getRequestValue(pjp, handleLog.getRequestType());
        if (StringUtils.isNotEmpty(requestParam)) {
            logger.info("{},请求参数:{}", handleLog.getTitle(), requestParam);
            handleLog.setRequestParam(requestParam);
        }
        try {
            Object respObject = pjp.proceed();
            String responseJson = JacksonUtils.toJson(respObject);
            logger.info("{},响应参数:{}", handleLog.getTitle(), responseJson);
            handleLog.setResponseParam(responseJson);
            ApiOutput<?> apiOutput = ((ApiOutput<?>) respObject);
            if (apiOutput.isSuccess()) {
                handleLog.setBlnSuccess("1");
            } else {
                handleLog.setBlnSuccess("0");
            }
            return respObject;
        } catch (Exception e) {
            logger.error("{}处理异常:{}", handleLog, e.getMessage());
            handleLog.setBlnSuccess("0");
            handleLog.setErrorDesc(e.getMessage());
            throw e;
        } finally {
            DelegateThreadLocal.remove();
            fillExpendTime(handleLog);
            applicationContext.publishEvent(new HandleFinishEvent(handleLog));
        }
    }

    private void fillRequestInfo(String className, String methodName, DelegateHandler delegateHandler,
        SysHandleLog handleLog) {
        String traceId = MDC.get("traceId");
        traceId = traceId.substring(traceId.indexOf(":") + 1, traceId.length() - 1);
        handleLog.setTraceId(traceId);
        handleLog.setTitle(delegateHandler.title());
        handleLog.setBusinessType(delegateHandler.businessType().toString());
        handleLog.setMethod(className + "." + methodName + "()");
        handleLog.setRequestType(ServletUtils.getRequest().getMethod());
        handleLog.setRequestIp(MDC.get("request_ip"));
        handleLog.setRequestUrl(MDC.get("request_url"));
    }

    private void fillExpendTime(@NonNull SysHandleLog sysHandleLog) {
        try {
            long endTime = System.currentTimeMillis();
            String requestStart = MDC.get("request_start_time");
            long startTime = Long.parseLong(requestStart);
            long requestExpendTime = endTime - startTime;
            logger.info("{},耗时:{}ms", sysHandleLog.getTitle(), requestExpendTime);
            sysHandleLog.setRequestStartTime(DateUtils.dateToString(new Date(startTime), "yyyy-MM-dd HH:mm:ss"));
            sysHandleLog.setRequestEndTime(DateUtils.dateToString(new Date(endTime), "yyyy-MM-dd HH:mm:ss"));
            sysHandleLog.setHandleDate(DateUtils.dateToString(new Date(), "yyyy-MM-dd"));
            sysHandleLog.setRequestExpandTime(requestExpendTime);
        } catch (Exception e) {
            logger.error("处理请求耗时异常:" + e.getMessage());
        }
    }

    private void fillUserInfo(SysHandleLog handleLog) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser) {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            handleLog.setLoginName(loginUser.getLoginName());
            handleLog.setUserName(loginUser.getUserName());
        }
    }

    private String getRequestValue(JoinPoint joinPoint, String requestType) {
        if (HttpMethod.PUT.name().equals(requestType) || HttpMethod.POST.name().equals(requestType)) {
            String params = argsArrayToString(joinPoint.getArgs());
            return StringUtils.substring(params, 0, 2000);
        } else {
            Map<?, ?> paramsMap = (Map<?, ?>) ServletUtils.getRequest()
                .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            return StringUtils.substring(paramsMap.toString(), 0, 2000);
        }
    }

    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null) {
            for (Object o : paramsArray) {
                if (o != null && !isFilterObject(o)) {
                    params.append(JacksonUtils.toJson(o)).append(" ");
                }
            }
        }
        return params.toString().trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
            || o instanceof BindingResult;
    }
}
