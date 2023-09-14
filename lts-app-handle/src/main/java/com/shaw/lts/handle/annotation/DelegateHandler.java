package com.shaw.lts.handle.annotation;

import com.shaw.lts.handle.enums.BusinessType;
import com.shaw.lts.handle.handler.AbstractApiHandler;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DelegateHandler
 * 委派 handler 注解
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/6/16 11:26
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DelegateHandler {

    /**
     * 处理器bean 名称
     * @return String
     */
    Class<? extends AbstractApiHandler<?, ?>> handler();

    /**
     * 标题
     * @return String
     */
    String title() default "";

    /**
     * 业务操作类型
     * @return {@link BusinessType}
     */
    BusinessType businessType() default BusinessType.OTHER;
}
