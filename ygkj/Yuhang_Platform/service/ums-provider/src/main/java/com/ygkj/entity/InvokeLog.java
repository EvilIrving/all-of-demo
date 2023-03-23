package com.ygkj.entity;

import lombok.Data;

import java.util.Date;

@Data
public class InvokeLog {

    /**
     * id
     */
    private Integer id;

    /**
     * 调用者ip
     */
    private String invokeIp;

    /**
     * 请求路径
     */
    private String requestPath;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求参数
     */
    private String invokeParam;

    /**
     * 请求用户
     */
    private String userId;

    /**
     * 请求时间
     */
    private Date invokeTime;

    /**
     * 请求类型 1增 2删 3改 4查
     */
    private Integer invokeType;
}
