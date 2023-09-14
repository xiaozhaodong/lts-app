package com.shaw.lts.core.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * FormativeException
 * 支持格式化字符串的自定义运行时异常
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/20 19:53
 **/
public class FormatRuntimeException extends RuntimeException {

    private int[] indices;
    private int usedCount;
    private String message;
    private transient Throwable throwable;

    public FormatRuntimeException() {
        super();
    }

    public FormatRuntimeException(String message) {
        this.message = message;
    }

    public FormatRuntimeException(Throwable cause) {
        this.throwable = cause;
        this.message = ExceptionUtils.getStackTrace(throwable);
    }

    public FormatRuntimeException(String format, Object... arguments) {
        init(format, arguments);
        fillInStackTrace();
        this.message = formatMessage(format, arguments);
        if (throwable != null) {
            this.message += System.lineSeparator() + ExceptionUtils.getStackTrace(throwable);
        }
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        String s = getClass().getName();
        String message = getMessage();
        return (message != null) ? (s + ": " + message) : s;
    }

    private void init(String format, Object... arguments) {
        // divide by 2
        final int len = Math.max(1, format == null ? 0 : format.length() >> 1);
        // LOG4J2-1542 ensure non-zero array length
        this.indices = new int[len];
        final int placeholders = ParameterFormatter.countArgumentPlaceholders2(format, indices);
        initThrowable(arguments, placeholders);
        this.usedCount = Math.min(placeholders, arguments == null ? 0 : arguments.length);
    }

    private void initThrowable(final Object[] params, final int usedParams) {
        if (params != null) {
            final int argCount = params.length;
            if (usedParams < argCount && this.throwable == null && params[argCount - 1] instanceof Throwable) {
                this.throwable = (Throwable) params[argCount - 1];
            }
        }
    }

    private String formatMessage(String format, Object... arguments) {
        StringBuilder stringBuilder = new StringBuilder();
        if (indices[0] < 0) {
            ParameterFormatter.formatMessage(stringBuilder, format, arguments, usedCount);
        } else {
            ParameterFormatter.formatMessage2(stringBuilder, format, arguments, usedCount, indices);
        }
        return stringBuilder.toString();
    }
}
