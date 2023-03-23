package com.dcy.datamanage.api.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/9/13 16:14
 */
@AllArgsConstructor
@Getter
public enum ApiErrorCode implements IErrorCode {
    /**
     * 失败
     */
    ERROR(0, "操作失败"),
    /**
     * 成功
     */
    SUCCESS(1, "操作成功"),

    /**
     * 校验公共错误
     */
    CHECK_ERROR(1000, "校验公共错误"),
    OTHER_ERROR(3000, "其他exe 错误"),
    ;

    private final Integer code;
    private final String msg;

    public static ApiErrorCode fromCode(Integer code) {
        return Stream.of(ApiErrorCode.values())
                .filter(apiErrorCode -> apiErrorCode.code.equals(code))
                .findAny()
                .orElse(SUCCESS);
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return String.format(" ErrorCode:{code=%s, msg=%s} ", code, msg);
    }
}