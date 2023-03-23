package com.ygkj.lcsyn.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-12-21 11:20
 */
@Data
public class BmSeawallStructinfo {
    /**
     * id
     */
    private String id;
    /**
     * 工程编码
     */
    private String prcd;
    /**
     * 防浪墙高度
     */
    private Double waveWallHigh;
    /**
     * 防浪墙高度
     */
    private Double waveWallElevation;
    /**
     * 防浪墙结构类型
     */
    private String waveWallType;
    /**
     * 防浪墙宽度
     */
    private Double waveWallWidth;
    /**
     * 闭气土方高程
     */
    private Double earthworkHigh;
    /**
     * 闭气土方坡比
     */
    private String earthworkSlope;
    /**
     * 闭气土方宽度
     */
    private Double earthworkSwidth;
    /**
     * 护面形式
     */
    private String armorType;
    /**
     * 护面迎水面坡比
     */
    private String armorUpwaterSlope;
    /**
     * 护面背水面坡比
     */
    private String armorDownwaterSlope;
    /**
     *
     镇压平台高度
     */
    private Double suplatformHigh;
    /**
     *
     镇压平台高程
     */
    private Double suplatformElevation;
    /**
     * 堤前亲水平台结构形式
     */
    private String dikePlatformType;
    /**
     * 堤前亲水平台高度
     */
    private Double dikePlatformHight;
    /**
     * 堤前亲水平台高程
     */
    private Double dikePlatformelevation;
    /**
     * 所属标段
     */
    private String bidSection;

    /**
     * 桩号
     */
    private String stake;

    /**
     * 桩号经度
     */
    private BigDecimal stakeLongitude;

    /**
     * 桩号纬度
     */
    private BigDecimal stakeLatitude;

    /**
     * 断面类型
     */
    private String sectionType;

    /**
     * 断面图
     */
    private String section;

    /**
     * 图号
     */
    private String drawingNo;
}
