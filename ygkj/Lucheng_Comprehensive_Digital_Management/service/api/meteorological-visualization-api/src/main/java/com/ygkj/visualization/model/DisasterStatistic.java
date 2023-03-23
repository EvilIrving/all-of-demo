package com.ygkj.visualization.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description 灾害汇总表
 * @date 2021-10-12
 */
@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class DisasterStatistic implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 灾害类型：台风/山洪/暴雨
     */
    private String type;

    /**
     * 灾害名称
     */
    private String disasterName;

    /**
     * 灾害编号
     */
    private String disasterCode;

    /**
     * 灾害发生年份
     */
    private Integer year;

    /**
     * 受灾人口（万人）
     */
    private Double affectedPopulation;

    /**
     * 经济损失（亿元）
     */
    private Double economicLosses;

    /**
     * 灾害描述
     */
    private String description;

    /**
     * 灾害附件
     */
    private String fileId;

    /**
     * 起始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    private String fileUrl;

    public DisasterStatistic() {
    }

}

