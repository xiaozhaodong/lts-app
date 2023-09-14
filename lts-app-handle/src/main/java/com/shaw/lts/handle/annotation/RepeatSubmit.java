package com.shaw.lts.handle.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RepeatSubmit
 * 重复提交检查
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/6/19 15:30
 **/
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {


}
