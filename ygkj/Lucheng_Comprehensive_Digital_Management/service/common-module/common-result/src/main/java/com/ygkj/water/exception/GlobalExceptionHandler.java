package com.ygkj.water.exception;

import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * @author: fml
 * @Date: 2020/8/13 11:17
 * @Description: 全局异常处理
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler implements InitializingBean {

    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public CommonResult handle(BusinessException e) {
        return CommonResult.failed(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public CommonResult handle(Exception e) {

        String defaultMsg = "系统错误";

        if (e instanceof CustomException) {
            CustomException ce = (CustomException) e;
            if (ce.getResultCode() != null) {
                throw ce;
            }
        }

        // 校验异常
        if (e instanceof BindException) {
            BindingResult bindingResult = ((BindException) e).getBindingResult();
            ObjectError objectError = bindingResult.getAllErrors().get(0);
            return CommonResult.failed(CommonCode.VALIDATE_FAILED, objectError.getDefaultMessage());
        }

        // Spring Validator参数校验异常处理
        if (e instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) e).getConstraintViolations();
            for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                String message = constraintViolation.getMessage();
                if (!StringUtils.isEmpty(message)) {
                    //直接返回第一个错误信息
                    defaultMsg = message;
                }
            }
            return CommonResult.failed(CommonCode.VALIDATE_FAILED, defaultMsg);
        }

        // Hibernate Validator参数校验异常处理
        if (e instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            ObjectError objectError = bindingResult.getAllErrors().get(0);
            return CommonResult.failed(CommonCode.VALIDATE_FAILED, objectError.getDefaultMessage());
        }
        e.printStackTrace();
        return CommonResult.failed(defaultMsg);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("全局异常处理器初始化完毕");
    }
}
