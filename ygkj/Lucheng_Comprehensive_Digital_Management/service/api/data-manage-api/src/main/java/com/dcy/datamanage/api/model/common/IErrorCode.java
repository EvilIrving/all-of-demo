package com.dcy.datamanage.api.model.common;

/**
 * @Author：dcy
 * @Description: 请求返回
 * @Date: 2021/9/14 13:05
 */
public interface IErrorCode {

    /**
     * 错误编码 1、成功;0、失败
     *
     * @return
     */
    Integer getCode();

    /**
     * 错误描述
     *
     * @return
     */
    String getMsg();

}
