package com.ygkj.gragh.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Huangzh
 * @description 堰闸信息表
 * @date 2020-12-09
 */
@Data
public class AttWasBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 闸门测站编码
     */
    private String stationCode;

    /**
     * 闸门名称
     */
    private String name;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 水闸编码
     */
    private String wagaCode;

    /**
     * 行政区划
     */
    private String areaCode;

    public AttWasBase() {
    }

}

