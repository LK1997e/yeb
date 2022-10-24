package com.example.server.config.Exception;

import com.example.server.pojo.RespBean;
import jdk.nashorn.internal.runtime.options.LoggingOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalException {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalException.class);

    @ExceptionHandler(SQLException.class)
    public RespBean mySqlException(SQLException e){
        LOGGER.error(e.getMessage());
        if(e instanceof SQLIntegrityConstraintViolationException){
            return RespBean.error("该数据有关联数据，操作失败！");
        }
        return RespBean.error("数据库异常，操作失败");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public RespBean myHttpMessageNotReadableException(HttpMessageNotReadableException e){
        LOGGER.error(e.getMessage());
        return RespBean.error("数据类型不匹配，操作失败");
    }
}
