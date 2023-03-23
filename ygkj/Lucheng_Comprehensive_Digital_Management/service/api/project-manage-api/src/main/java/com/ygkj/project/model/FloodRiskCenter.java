package com.ygkj.project.model;

import lombok.Data;

/**
 * @author wgf
 * @date 2022/3/10 15:06
 * @Description:
 */
@Data
public class FloodRiskCenter {

    /**
     * 水库风险
     */
    private Integer skRisk = 0;
    /**
     * 水闸风险
     */
    private Integer szRisk = 0;
    /**
     * 海塘风险
     */
    private Integer htRisk = 0;
    /**
     * 堤防风险
     */
    private Integer dfRisk = 0;
}
