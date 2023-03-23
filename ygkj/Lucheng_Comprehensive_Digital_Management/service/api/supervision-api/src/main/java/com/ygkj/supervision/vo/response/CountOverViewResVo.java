package com.ygkj.supervision.vo.response;


import lombok.Data;

/**
 * @author lxl
 */
@Data
public class CountOverViewResVo {

    /**
     * 工程总数
     */
    private Integer projectTotal;

    /**
     * 事项总数
     */
    private Integer matterTotal;

    /**
     * 问题总数
     */
    private Integer problemTotal;

    /**
     * 整改问题总数
     */
    private Integer rectificationTotal;

    /**
     * 未整改问题总数
     */
    private Integer unRectificationTotal;

    /**
     * 检查整改率
     */
    private Double rectificationRate;

}
