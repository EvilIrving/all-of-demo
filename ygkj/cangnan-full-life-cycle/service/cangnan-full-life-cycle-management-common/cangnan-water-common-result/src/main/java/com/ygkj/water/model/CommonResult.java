package com.ygkj.water.model;

import java.io.Serializable;

/**
 * 通用返回对象
 */
public class CommonResult<T> implements Serializable {
    private long code;
    private String message;
    private T data;

    protected CommonResult() {
    }

    protected CommonResult(long code, String message){
        this(code,message,null);
    }

    protected CommonResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static CommonResult success(){
        return new CommonResult(CommonCode.SUCCESS.code(), CommonCode.SUCCESS.message());
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(CommonCode.SUCCESS.code(), CommonCode.SUCCESS.message(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<T>(CommonCode.SUCCESS.code(), message, data);
    }

    /**
     * 失败返回结果
     *
     * @param resultCode 错误码
     */
    public static <T> CommonResult<T> failed(ResultCode resultCode) {
        return new CommonResult<T>(resultCode.code(), resultCode.message(), null);
    }

    /**
     * 失败返回结果
     *
     * @param resultCode 错误码
     * @param message    错误信息
     */
    public static <T> CommonResult<T> failed(ResultCode resultCode, String message) {
        return new CommonResult<T>(resultCode.code(), message, null);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<T>(CommonCode.FAILED.code(), message, null);
    }

    /**
     * 失败返回结果
     */
    public static <T> CommonResult<T> failed() {
        return failed(CommonCode.FAILED);
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> CommonResult<T> validateFailed() {
        return failed(CommonCode.VALIDATE_FAILED);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> CommonResult<T> validateFailed(String message) {
        return new CommonResult<T>(CommonCode.VALIDATE_FAILED.code(), message, null);
    }

    /**
     * 未登录返回结果
     */
    public static <T> CommonResult<T> unauthorized(T data) {
        return new CommonResult<T>(CommonCode.UNAUTHORIZED.code(), CommonCode.UNAUTHORIZED.message(), data);
    }

    /**
     * 未授权返回结果
     */
    public static <T> CommonResult<T> forbidden(T data) {
        return new CommonResult<T>(CommonCode.FORBIDDEN.code(), CommonCode.FORBIDDEN.message(), data);
    }

    /**
     * 流量控制返回结果
     */
    public static <T> CommonResult<T> flowLimited(){
        return new CommonResult(CommonCode.FLOW_LIMITED.code(), CommonCode.FLOW_LIMITED.message());
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CommonResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
