package com.ygkj.water.exception;


/**
 * @author: fml
 * @Date: 2020/8/13 13:43
 * @Description: 自定义异常
 */
public class CustomException extends RuntimeException {

    private com.ygkj.water.model.ResultCode resultCode;

    public CustomException(com.ygkj.water.model.ResultCode resultCode) {
        super(resultCode.message());
        this.resultCode = resultCode;
    }

    public CustomException(String message) {
        super(message);
    }

    public com.ygkj.water.model.ResultCode getResultCode() {
        return resultCode;
    }
}
