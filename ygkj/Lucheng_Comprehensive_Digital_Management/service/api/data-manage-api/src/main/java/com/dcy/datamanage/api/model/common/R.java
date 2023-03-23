package com.dcy.datamanage.api.model.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/**
 * @Author：dcy
 * @Description: 返回给前台的通用包装
 * @Date: 2019/8/9 11:40
 */
@Getter
@Setter
@ApiModel(value = "R", description = "返回数据包装")
public class R<T> {

    @ApiModelProperty(value = "响应状态码", notes = "成功1，失败0")
    private Integer code;

    @ApiModelProperty(value = "响应信息")
    private String msg;

    @ApiModelProperty(value = "响应对象")
    private T data;

    public R() {

    }

    public R(IErrorCode errorCode) {
        errorCode = Optional.ofNullable(errorCode).orElse(ApiErrorCode.ERROR);
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public static <T> R<T> success(T data) {
        ApiErrorCode aec = ApiErrorCode.SUCCESS;
        if (data instanceof Boolean && Boolean.FALSE.equals(data)) {
            aec = ApiErrorCode.ERROR;
        }
        return restResult(aec, data);
    }

    public static <T> R<T> error(String msg) {
        return restResult(ApiErrorCode.ERROR.getCode(), msg, null);
    }

    public static <T> R<T> error(IErrorCode errorCode) {
        return restResult(errorCode, null);
    }

    public static <T> R<T> restResult(IErrorCode errorCode, T data) {
        return restResult(errorCode.getCode(), errorCode.getMsg(), data);
    }

    private static <T> R<T> restResult(Integer code, String msg, T data) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }
}
