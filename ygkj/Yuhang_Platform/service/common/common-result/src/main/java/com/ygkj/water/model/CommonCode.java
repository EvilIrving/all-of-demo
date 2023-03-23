package com.ygkj.water.model;

import lombok.ToString;

/**
 * @author: fml
 * @Date: 2020/8/13 11:30
 * @Description: 结果枚举信息
 */

@ToString
public enum CommonCode implements ResultCode {

    SUCCESS(0, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(400, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限");

    /**
     * 提示码
     */
    private int code;

    /**
     * 提示信息
     */
    private String message;

    private CommonCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
