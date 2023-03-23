package com.ygkj.digitization.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 预警潮位发送信息实体类
 */
@Data
public class TideWarnSendInfo implements Serializable {

    // 报警类型 9:潮位警报-当前潮位超过（）米显示为蓝色预警10:当前潮位超过（）米显示为黄色预警
    // 11:潮位警报-当前潮位超过（）米显示为橙色预警 12:潮位警报-当前潮位超过（）米显示为红色预警
    private String type;

    // 警戒值
    private Double overData;

    // 行政区划名称
    private String adNm;

    // 行政区划编码
    private String adcd;

    // 潮位
    private Double tdz;

    //测站名称
    private String stName;

    //测站编码
    private String stcd;
}
