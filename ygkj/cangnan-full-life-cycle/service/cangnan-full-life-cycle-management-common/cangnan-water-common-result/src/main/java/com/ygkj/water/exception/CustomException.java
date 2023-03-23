package com.ygkj.water.exception;

import com.ygkj.water.model.ResultCode;

public class CustomException extends RuntimeException {

    private ResultCode resultCode;

    public CustomException(ResultCode resultCode) {
        super(resultCode.message());
        this.resultCode = resultCode;
    }

    public CustomException(String message) {
        super(message);
    }

    public ResultCode getResultCode(){
        return resultCode;
    }
}
