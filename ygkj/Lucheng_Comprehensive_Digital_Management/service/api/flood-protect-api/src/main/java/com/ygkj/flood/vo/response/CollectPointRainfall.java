package com.ygkj.flood.vo.response;

import lombok.Data;

/**
 * @author xq
 * @Date 2021/3/16
 */
@Data
public class CollectPointRainfall {
    /**
     * id
     */
    private String id;

    /**
     * 点位名称
     */
    private String pointName;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 地址
     */
    private String location;

    /**
     * 1小时累计雨量
     */
    private Double oneHourRainfall;
    /**
     * 3小时累计雨量
     */
    private Double threeHourRainfall;
    /**
     * 6小时累计雨量
     */
    private Double sixHourRainfall;
    /**
     * 24小时累计雨量
     */
    private Double twentyFourHourRainfall;
    /**
     * 72小时累计雨量
     */
    private Double seventyTwoHourRainfall;
}
