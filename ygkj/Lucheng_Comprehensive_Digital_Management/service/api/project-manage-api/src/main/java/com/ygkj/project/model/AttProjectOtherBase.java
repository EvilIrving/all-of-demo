package com.ygkj.project.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * 其他工程基础信息表
 *
 * @author 大狼狗 2020-12-29
 */
@Data
public class AttProjectOtherBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工程编码
     */
    private String projectCode;

    /**
     * 工程名称
     */
    private String projectName;

    /**
     * 所属流域
     */
    private String bas;

    /**
     * 行政区划
     */
    private String areaCode;

    /**
     * 政府负责人用户
     */
    private String govermentChargeId;

    /**
     * 水行政部门负责人用户
     */
    private String waterDepartmentChargeId;

    /**
     * 主管部门负责人
     */
    private String competentDepartmentChargeId;

    /**
     * 管理单位责任人
     */
    private String manageDepartmentChargeId;

    /**
     * 技术责任人
     */
    private String technologyChargeId;

    /**
     * 巡查责任人
     */
    private String patrolChargeId;

    /**
     * 删除标记 0未删除 1已删除
     */
    private boolean delFlag;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    public AttProjectOtherBase() {
    }

}
