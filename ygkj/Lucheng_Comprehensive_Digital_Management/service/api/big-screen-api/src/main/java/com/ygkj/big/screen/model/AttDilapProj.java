package com.ygkj.big.screen.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xq
 * @description 病险工程表
 * @date 2021-05-15
 */
@Data
public class AttDilapProj implements Serializable {


    private static final long serialVersionUID = -2282935869892964163L;
    /**
     * id
     */
    private String id;

    /**
     * 工程名称
     */
    private String projectName;

    /**
     * 工程编码
     */
    private String projectCode;

    /**
     * 工程类型：水库/海塘/水闸/山塘
     */
    private String projectType;

    /**
     * 最后一次安全鉴定时间
     */
    private String lastCheckTime;

    /**
     * 最后一次安全鉴定结果
     */
    private String lastCheckResult;

    public AttDilapProj() {
    }

}
