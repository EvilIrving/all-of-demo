package com.ygkj.exception.handlers;

import com.ygkj.exception.exceptions.OperationNotAllowedException;
import com.ygkj.exception.exceptions.PermissionDeniedException;
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
public class GlobalExceptionHandler implements InitializingBean {

    private static final String ERROR_DELIMITER = "&";

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public CommonResult IllegalArgumentExceptionHandle(IllegalArgumentException exception) {
        String message = exception.getMessage();
        String candidateMessage;
        if (message.contains(ERROR_DELIMITER)) {
            String[] split = message.split(ERROR_DELIMITER);
            candidateMessage = "当前参数" + split[0] + "校验不正确，请核验该参数的值：" + split[1];
        } else {
            candidateMessage = message;
        }
        return CommonResult.validateFailed(candidateMessage);
    }

    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    @ResponseBody
    public CommonResult MyExceptionHandle(BindException exception) {
//        exception.printStackTrace();
        BindingResult result = exception.getBindingResult();
        StringBuilder errorMsg = new StringBuilder();
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(error -> {
                errorMsg.append(error.getDefaultMessage()).append("!");
            });
        }
//        exception.printStackTrace();
        return CommonResult.failed(CommonCode.VALIDATE_FAILED, errorMsg.toString());
    }

    @ExceptionHandler(value = {OperationNotAllowedException.class})
    @ResponseBody
    public CommonResult OperationNotAllowedExceptionHandle(OperationNotAllowedException exception) {
        return CommonResult.failed(CommonCode.FAILED, exception.getMessage());
    }


    @ExceptionHandler(value = {PermissionDeniedException.class})
    @ResponseBody
    public CommonResult MyRuntimeExceptionHandle(PermissionDeniedException exception) {
        return CommonResult.failed(CommonCode.FORBIDDEN, exception.getMessage());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("------全局异常处理器初始化完毕------");
    }
}
