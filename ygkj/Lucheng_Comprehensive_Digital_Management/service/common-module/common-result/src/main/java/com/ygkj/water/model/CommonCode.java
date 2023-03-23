package com.ygkj.water.model;

import lombok.ToString;

/**
 * @author: fml
 * @Date: 2020/8/13 11:30
 * @Description: 结果枚举信息
 */

@ToString
public enum CommonCode implements com.ygkj.water.model.ResultCode {

    SUCCESS(0, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(400, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    VERIFICATION_CODE_FAILED(402,"验证码验证失败"),
    FORBIDDEN(403, "没有相关权限"),
    NOT_FOUND(404, "没有找到网站来路"),
    TABLENOAUTH(214, "没有表权限"),
    TABLEAPPLYING(215, "数据表申请中"),
    FLOW_LIMITED(410, "操作过于频繁，请稍后再试！"),
    NOT_ACCOUNT(401004, "该用户不存在,请先注册");

    /**
     * 提示码
     */
    private final int code;

    /**
     * 提示信息
     */
    private final String message;

    CommonCode(int code, String message) {
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
