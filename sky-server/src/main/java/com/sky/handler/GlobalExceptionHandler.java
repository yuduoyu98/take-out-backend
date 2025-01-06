package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result<String> exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler
    public Result<String> duplicateUsername(SQLIntegrityConstraintViolationException ex){
        log.error("异常信息：{}", ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry") && ex.getMessage().contains("for key 'idx_username'")) {
            String username = ex.getMessage().split(" ")[2];
            return Result.error("用户名" + username + "已存在");
        }
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }
}
