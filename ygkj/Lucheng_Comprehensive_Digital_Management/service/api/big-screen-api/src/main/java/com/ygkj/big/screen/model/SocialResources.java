package com.ygkj.big.screen.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SocialResources  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 高程
     */
    private Double elevation;

    /**
     * 行政区划编码(这个编码与sub_adcd对的上，与att_adcd_base部分对的上)
     */
    private String adcd;

    /**
     * 行政区划
     */
    private String adnm;

    /**
     * 地址
     */
    private String address;
}
