package com.ygkj.exception.handlers;

import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/25 10:15
 * @Description: 全局异常处理类
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler implements InitializingBean{

    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    @ResponseBody
    public CommonResult MyExceptionHandle(BindException exception){
        exception.printStackTrace();
        BindingResult result = exception.getBindingResult();
        StringBuilder errorMsg = new StringBuilder() ;
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(error -> {
                errorMsg.append(error.getDefaultMessage()).append("!");
            });
        }
        exception.printStackTrace();
        return CommonResult.failed(CommonCode.VALIDATE_FAILED,errorMsg.toString() );
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("------全局异常处理器初始化完毕------");
    }
}
