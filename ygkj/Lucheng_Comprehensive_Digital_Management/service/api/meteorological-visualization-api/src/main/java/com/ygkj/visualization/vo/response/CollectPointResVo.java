package com.ygkj.visualization.vo.response;

import lombok.Data;

@Data
public class CollectPointResVo {

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
     * 六小时累计雨量
     */
    private Double sixHourRainfall;


}
