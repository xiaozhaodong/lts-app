package com.shaw.lts.core.exception;

/**
 * JacksonException
 * Json处理异常
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/20 19:59
 **/
public class JacksonException extends FormatRuntimeException {

    public JacksonException() {
        super();
    }

    public JacksonException(String message) {
        super(message);
    }

    public JacksonException(Throwable cause) {
        super(cause);
    }

    public JacksonException(String format, Object... arguments) {
        super(format, arguments);
    }
}
