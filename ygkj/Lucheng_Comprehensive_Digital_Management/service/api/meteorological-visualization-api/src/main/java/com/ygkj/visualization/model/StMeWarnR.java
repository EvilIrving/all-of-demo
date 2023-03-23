package com.ygkj.visualization.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

/**
 * @author Huangzh
 * @description 气象预警实时监测表
 * @date 2021-09-13
 */
@Data
@ApiModel("气象预警实时监测表")
public class StMeWarnR implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预警编号
     */
    @ApiModelProperty("预警编号")
    private String warnCode;

    /**
     * 行政区划代码
     */
    @ApiModelProperty("行政区划代码")
    private String areaCode;

    /**
     * 经度，用于地图落点
     */
    @ApiModelProperty("经度，用于地图落点")
    private String longitude;

    /**
     * 纬度，用于地图落点
     */
    @ApiModelProperty("纬度，用于地图落点")
    private String latitude;

    /**
     * 发布气象预警的单位
     */
    @ApiModelProperty("发布气象预警的单位")
    private String publishUnit;

    /**
     * 预警影响的行政区域编码
     */
    @ApiModelProperty("预警影响的行政区域编码")
    private String warnArea;

    /**
     * 预警内容
     */
    @ApiModelProperty("预警内容")
    private String warnContent;

    /**
     * 预警等级
     */
    @ApiModelProperty("预警等级")
    private String warnLevel;

    /**
     * 预警防范措施
     */
    @ApiModelProperty("预警防范措施")
    private String warnMeasure;

    /**
     * 预警优先级
     */
    @ApiModelProperty("预警优先级")
    private String warnPeriod;

    /**
     * 预警类型
     */
    @ApiModelProperty("预警类型")
    private String warnType;

    /**
     * 推送时间
     */
    @ApiModelProperty("推送时间")
    private Date publishDate;

    /**
     * 预警推送标题
     */
    @ApiModelProperty("预警推送标题")
    private String title;

    public StMeWarnR() {
    }

    public StMeWarnR(String warnCode, String areaCode,
                     String longitude, String latitude,
                     String publishUnit, String warnArea,
                     String warnContent, String warnLevel,
                     String warnMeasure, String warnPeriod,
                     String warnType, Date publishDate, String title) {
        this.warnCode = warnCode;
        this.longitude = longitude;
        this.latitude = latitude;
        this.publishUnit = publishUnit;
        this.warnArea = warnArea;
        this.warnContent = warnContent;
        this.warnLevel = warnLevel;
        this.warnMeasure = warnMeasure;
        this.warnPeriod = warnPeriod;
        this.warnType = warnType;
        this.publishDate = publishDate;
        this.title = title;
        //行政区划编码补齐
        Optional.ofNullable(areaCode).ifPresent(code -> {
            if (12 > code.length()) {
                char[] chars = new char[12];
                Arrays.fill(chars, '0');
                for (int i = 0; i < code.length(); i++) {
                    chars[i] = code.charAt(i);
                }
                this.areaCode = new String(chars);
            } else if (12 == code.length()) {
                this.areaCode = code;
            }
        });
    }
}

