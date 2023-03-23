package com.ygkj.big.screen.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * @author wgf
 * @date 2022/8/31 16:04
 * @Description: 戌浦江工程调度工程列表实体
 */
@Data
public class XpjProject {

    /**
     * 名称（测站名称，断面名称）
     */
    private String name;

    /**
     * 水文监测站代码
     */
    private String stCode;

    /**
     * 经度
     */
    private String lgtd;

    /**
     * 纬度
     */
    private String lttd;

    /**
     * 汛限水位
     */
    private Double limitLevel;

    /**
     * 保证水位
     */
    private Double guaranteeLevel;

    /**
     * 警戒水位
     */
    private Double warningLevel;

    /**
     * 实时水位（潮位）数据
     */
    private Double waterLevel;

    /**
     * 水位的更新时间
     */
    private Date updateTime;

    /**
     * 预报高潮位数据
     */
    private Double ybTdz;

    /**
     * 预报高潮位时间
     */
    private Date ybTime;

    /**
     * 断面起始,结束位置
     */
    private String coordinates;

    /**
     * 闸上水位
     */
    private Double upz;
    /**
     * 闸下水位
     */
    private Double dwz;

    /**
     * type  断面测站，水闸测站，水库测站，潮位测站，
     */
    private String type;

    /**
     * 闸门开度
     */
    private String openGate;

    /**
     * 断面状态（0.正常，1.超保证，2超警戒）
     */
    private String status;

    /**
     * 断面id
     */
    private String dmId;

}
