package com.ygkj.gragh.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 水文概化图整编数据表
 * @date 2021-06-02
 */
@Data
public class AttHydrDiagBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 概化图点位名称
     */
    private String name;

    /**
     * 测站工程编码
     */
    private String code;

    /**
     * 编码类型: 水库工程，水闸工程，潮位测站，河道测站
     */
    private String codeType;

    /**
     * 流域名称
     */
    private String bas;

    private Integer px;

    public AttHydrDiagBase() {
    }

}
