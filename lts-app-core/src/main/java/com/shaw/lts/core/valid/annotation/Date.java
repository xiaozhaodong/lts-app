package com.shaw.lts.core.valid.annotation;

import com.shaw.lts.core.valid.validator.DateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日期格式校验
 * @author xiaozhaodong
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {DateValidator.class})
public @interface Date {

    String message() default "日期格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String format();
}
