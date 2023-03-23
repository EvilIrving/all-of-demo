package com.ygkj.supervision.vo.response;

import lombok.Data;

/**
 * 统计分析 回参
 *
 * @author lxl
 */
@Data
public class ProjectCountResVo {

    /**
     * 工程编码
     */
    private String projectCode;

    /**
     * 工程名称
     */
    private String projectName;

    /**
     * 工程个数
     */
    private Integer projectTotal;

    /**
     * 行政区划名称
     */
    private String areaName;

    /**
     * 行政区划编码
     */
    private String areaCode;

    /**
     * 检查开展总数
     */
    private Integer censorTotal;

    /**
     * 检查人次
     */
    private Integer censorPersonTotal;

    /**
     * 问题数量
     */
    private Integer censorProblemTotal;

    /**
     * 整改率
     */
    private Double rectificationRate;

    /**
     * 检查事项
     */
    private String matterType;

    /**
     * 追究类型
     */
    private String investigateType;

}
