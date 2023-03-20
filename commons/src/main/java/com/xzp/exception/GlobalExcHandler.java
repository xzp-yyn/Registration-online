package com.xzp.exception;

import com.xzp.result.Result;
import com.xzp.result.ResultCode;
import com.xzp.util.YyghException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/10/26 11:38
 * @Version 1.0
 */
@ControllerAdvice
public class GlobalExcHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result controllerExcption(Exception e){
        return Result.fail(e.getClass());
    }

    @ExceptionHandler(YyghException.class)
    @ResponseBody
    public Result yyghException(YyghException e){
        return Result.fail(e.getCode(),e.getMessage());
    }
}
