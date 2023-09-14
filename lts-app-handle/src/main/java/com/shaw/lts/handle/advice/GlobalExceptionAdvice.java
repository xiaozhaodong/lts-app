package com.shaw.lts.handle.advice;


import com.shaw.lts.handle.demain.ApiOutput;
import com.shaw.lts.handle.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintStream;
import java.nio.file.NoSuchFileException;

/**
 * GlobalExceptionHandler
 * 全局异常处理
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2022/7/19 15:07
 **/
@RestControllerAdvice
public class GlobalExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionAdvice.class);


    @ExceptionHandler(RuntimeException.class)
    public ApiOutput<?> exceptionHandler(RuntimeException e) {
        logger.error("RuntimeException:{}", stacktraceToString(e, 500));
        ApiOutput<?> output  = ApiOutput.fail().message(e.getMessage());
        logger.info(output.json());
        return output;
    }

    @ExceptionHandler(BusinessException.class)
    public ApiOutput<?> exceptionHandler(BusinessException e) {
        logger.error("BusinessException:{}", stacktraceToString(e, 500));
        ApiOutput<?> output  = ApiOutput.fail().message(e.getMessage());
        logger.info(output.json());
        return output;
    }

    @ExceptionHandler(NoSuchFileException.class)
    public ApiOutput<?> noSuchFileHandler(NoSuchFileException e) {
        logger.error("NoSuchFileException:{}", stacktraceToString(e, 500));
        ApiOutput<?> output  = ApiOutput.fail().message("文件不存在");
        logger.info(output.json());
        return output;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiOutput<?> exceptionHandler(MethodArgumentNotValidException e) {
//        logger.error("MethodArgumentNotValidException:{}", stacktraceToString(e, 2000));
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder("参数校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append(":").append(fieldError.getDefaultMessage()).append(",");
        }
        String result = sb.substring(0, sb.length() - 1);
        ApiOutput<?> output  = ApiOutput.fail().message(result);
        logger.info(output.json());
        return output;
    }

    @ExceptionHandler(Exception.class)
    public ApiOutput<?> exceptionHandler(Exception e) {
        logger.error("Exception:{}", stacktraceToString(e));
        ApiOutput<?> output  = ApiOutput.fail().message(e.getMessage());
        logger.info(output.json());
        return output;
    }

    private String stacktraceToString(Throwable throwable) {
         return stacktraceToString(throwable, 1000);
    }

    private String stacktraceToString(Throwable throwable, int limit) {
        FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(outputStream));
        String exceptionStr = outputStream.toString();
        int length = exceptionStr.length();
        if (limit < 0 || limit > length) {
            limit = length;
        }
        return limit == length ? exceptionStr : exceptionStr.substring(0, limit);
    }
}
