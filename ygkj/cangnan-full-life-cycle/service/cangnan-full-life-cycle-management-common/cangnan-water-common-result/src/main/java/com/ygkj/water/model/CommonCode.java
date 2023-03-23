package com.ygkj.water.model;

import lombok.ToString;

@ToString
public enum CommonCode implements ResultCode {

    SUCCESS(0, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(400, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    TABLENOAUTH(214, "没有表权限"),
    TABLEAPPLYING(215, "数据表申请中"),
    FLOW_LIMITED(410, "操作过于频繁，请稍后再试！");

    /**
     * 提示码
     */
    private final int code;

    /**
     * 提示信息
     */
    private final String message;

    CommonCode(int code, String message){
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
