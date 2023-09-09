package com.zisheng.Exception;

import com.zisheng.Pojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 */
@RestControllerAdvice// 表明该类为全局异常处理器，可以自动将返回的对象转换为JSON格式的字符串，响应给前端
public class ExceptionHandle {
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandle.class);// 日志记录对象
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)// 指定拦截到的异常的类型
    public Result handleException(SQLIntegrityConstraintViolationException e)
    {
        log.error(e.getMessage());// 将异常信息打印出来
        String excInfo = e.getMessage();
        if(excInfo.contains("Duplicate entry"))
        {
            String[] split = excInfo.split(" ");
            String retInfo = split[2] + "已存在";
            return Result.error(retInfo);
        }
        return Result.error("未知错误");
    }
}
