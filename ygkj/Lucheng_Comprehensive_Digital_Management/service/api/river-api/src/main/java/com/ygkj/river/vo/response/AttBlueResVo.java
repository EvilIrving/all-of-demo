package com.ygkj.river.vo.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lxl
 * @create 2022-02-25 13:52
 * @description
 */
@Data
public class AttBlueResVo {

    /**
     * 行政区划名称
     */
    private String areaName;

    /**
     * 监察次数
     */
    private Integer inspectionNumber;

    /**
     * 完成率
     */
    private BigDecimal finishedRate;

    /**
     * 排序
     */
    private Integer orderFlag;
}
