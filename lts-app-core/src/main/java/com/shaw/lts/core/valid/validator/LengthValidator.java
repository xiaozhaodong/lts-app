package com.shaw.lts.core.valid.validator;

import com.shaw.lts.core.valid.annotation.Length;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 请求参数长度校验
 * @author xiaozhaodong
 */
public class LengthValidator implements ConstraintValidator<Length, String> {

    private int value;

    @Override
    public void initialize(Length constraintAnnotation) {
        this.value = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || ("").equals(s)) {
            return true;
        }
        return s.length() <= value;
    }
}
