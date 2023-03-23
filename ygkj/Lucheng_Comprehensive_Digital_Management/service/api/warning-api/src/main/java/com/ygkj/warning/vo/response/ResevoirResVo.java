package com.ygkj.warning.vo.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ResevoirResVo {

    /**
     * 水库编码
     */
    private String resCode;

    /**
     * 水库名称
     */
    private String resName;


    /**
     * 梅汛期限制水位
     */
    private BigDecimal meiFloodLimitWaterLevel;

    /**
     * 政府负责人用户id
     */
    private String govermentChargeId;

    /**
     * 水行政部门负责人用户id
     */
    private String waterDepartmentChargeId;

    /**
     * 主管部门负责人id
     */
    private String competentDepartmentChargeId;

    /**
     * 管理单位责任人id
     */
    private String manageDepartmentChargeId;

    /**
     * 技术责任人id
     */
    private String technologyChargeId;

    /**
     * 巡查责任人id
     */
    private String patrolChargeId;
}
