package com.ygkj.project.model;

import lombok.Data;

@Data
public class AttSixchargeBase {

    /**
     * id
     */
    private String id;

    /**
     * 工程编码
     */
    private String projectCode;

    /**
     * 工程类型
     */
    private String projectType;

    /**
     * 政府负责人姓名
     */
    private String govermentChargeName;

    /**
     * 政府负责人电话
     */
    private String govermentChargePhone;

    /**
     * 政府负责人职务
     */
    private String govermentChargeJob;

    /**
     * 政府责任人部门
     */
    private String govermentChargeDept;

    /**
     * 水行政部门负责人姓名
     */
    private String waterDepartmentChargeName;

    /**
     * 水行政部门负责人电话
     */
    private String waterDepartmentChargePhone;

    /**
     * 水行政部门负责人职务
     */
    private String waterDepartmentChargeJob;

    /**
     * 水行政部门负责人部门
     */
    private String waterDepartmentChargeDept;

    /**
     * 主管部门负责人姓名
     */
    private String competentDepartmentChargeName;

    /**
     * 主管部门负责人电话
     */
    private String competentDepartmentChargePhone;

    /**
     * 主管部门负责人职务
     */
    private String competentDepartmentChargeJob;

    /**
     * 主管部门负责人部门
     */
    private String competentDepartmentChargeDept;

    /**
     * 管理单位责任人姓名
     */
    private String manageDepartmentChargeName;

    /**
     * 管理单位责任人电话
     */
    private String manageDepartmentChargePhone;

    /**
     * 管理单位责任人职务
     */
    private String manageDepartmentChargeJob;

    /**
     * 管理单位责任人部门
     */
    private String manageDepartmentChargeDept;

    /**
     * 技术责任人姓名
     */
    private String technologyChargeName;

    /**
     * 技术责任人电话
     */
    private String technologyChargePhone;

    /**
     * 技术责任人职务
     */
    private String technologyChargeJob;

    /**
     * 技术责任人部门
     */
    private String technologyChargeDept;

    /**
     * 巡查责任人姓名
     */
    private String patrolChargeName;

    /**
     * 巡查责任人电话
     */
    private String patrolChargePhone;

    /**
     * 巡查责任人职务
     */
    private String patrolChargeJob;

    /**
     * 巡查责任人部门
     */
    private String patrolChargeDept;

    /**
     * 是否有管理单位
     */
    private String ifDepartment;

    /**
     * 责任人公示文件名称
     */
    private String personPublishFilnm;

    /**
     * 责任人公示文件
     */
    private String personPublishFile;

    /**
     * 水库现场照片
     */
    private String reservoirPhoto;
}
