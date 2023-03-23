package com.ygkj.platform.management.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Huangzh
 * @description 系统api访问参数记录
 * @date 2021-09-24
 */
@Data
public class SysApiParamLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * api访问id
     */
    private Long apiLogId;

    /**
     * 参数名称
     */
    private String parameterName;

    /**
     * 参数值
     */
    private String parameterValue;

    private String userId;

    public SysApiParamLog() {
    }

    public SysApiParamLog(Long apiLogId, String parameterName, String parameterValue, String userId) {
        this.apiLogId = apiLogId;
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
        this.userId = userId;
    }
}
