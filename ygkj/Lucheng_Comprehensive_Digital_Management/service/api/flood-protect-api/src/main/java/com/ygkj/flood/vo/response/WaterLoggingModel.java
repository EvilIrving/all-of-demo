package com.ygkj.flood.vo.response;

import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/7/21
 */
@Data
public class WaterLoggingModel {

    /**
     * 积水点位名称
     */
    String waterLoggingName;

    /**
     * 表达式
     */
    String expression;

    /**
     * 生效月份
     */
    int effectMonth;

    /**
     * 生效阈值
     */
    double effectThreshold;

    public WaterLoggingModel(String waterLoggingName, String expression, int effectMonth, double effectThreshold) {
        this.waterLoggingName = waterLoggingName;
        this.expression = expression;
        this.effectMonth = effectMonth;
        this.effectThreshold = effectThreshold;
    }
}
