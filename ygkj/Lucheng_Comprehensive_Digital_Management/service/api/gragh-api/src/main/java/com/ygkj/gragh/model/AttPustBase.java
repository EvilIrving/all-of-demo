package com.ygkj.gragh.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Huangzh
 * @description 泵站基础信息表
 * @date 2020-12-09
 */
@Data
public class AttPustBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 泵站代码
     */
    private String pustCode;

    /**
     * 泵站名称
     */
    private String pustName;

    /**
     * 泵站经度
     */
    private BigDecimal pustLong;

    /**
     * 泵站维度
     */
    private BigDecimal pustLat;

    /**
     * 泵站水闸位置
     */
    private String pustLoc;

    /**
     * 泵站类型
     */
    private String pustType;

    /**
     * 装机流量
     */
    private BigDecimal insFlow;

    /**
     * 装机功率
     */
    private BigDecimal insPow;

    /**
     * 水泵数量
     */
    private Integer pumpNum;

    /**
     * 设计扬程
     */
    private BigDecimal desHead;

    /**
     * 工程任务
     */
    private String engTask;

    /**
     * 工程等别
     */
    private String engGrad;

    /**
     * 工程规模
     */
    private String engScal;

    /**
     * 主要建筑物级别
     */
    private String mainBuildGrad;

    /**
     * 工程建设情况
     */
    private String engStart;

    /**
     * 开工时间
     */
    private Date startDate;

    /**
     * 建成时间
     */
    private Date builtdate;

    /**
     * 竣工验收时间
     */
    private Date compDate;

    /**
     * 归口管理部门
     */
    private String admDep;

    /**
     * 是否通过标准化管理创建
     */
    private String staMan;

    /**
     * 主要构筑物
     */
    private String mainbuilding;

    /**
     * 是否竣工验收 （非加固）
     */
    private String isComplete;

    /**
     * 是否为闸站工程
     */
    private String isGateStationProject;

    /**
     * 泵站参照水位站编码
     */
    private String pumpReferenceWlStationCode;

    /**
     * 泵站参照水位站名称
     */
    private String pumpReferenceWlStationName;

    /**
     * 泵站参照水位站警戒水位
     */
    private BigDecimal pumpReferenceWlStationWarningLevel;

    /**
     * 泵站参照水位站危急水位
     */
    private BigDecimal pumpReferenceWlStationCriticalLevel;

    /**
     * 内河常水位(米)
     */
    private BigDecimal riverWaterLevel;

    /**
     * 电动机台数(台)
     */
    private Integer motorNumber;

    /**
     * 备注
     */
    private String note;

    /**
     * 记录生效时间
     */
    private Date effDate;

    /**
     * 记录失效时间
     */
    private Date exprDate;

    /**
     * 政府负责人用户id，关联到用户体系
     */
    private String govermentChargeId;

    /**
     * 水行政部门负责人用户id，关联到用户体系
     */
    private String waterDepartmentChargeId;

    /**
     * 主管部门负责人id，关联到用户体系
     */
    private String competentDepartmentChargeId;

    /**
     * 管理单位责任人id，关联到用户体系
     */
    private String manageDepartmentChargeId;

    /**
     * 技术责任人id，关联到用户体系
     */
    private String technologyChargeId;
    /**
     * 巡查责任人id，关联到用户体系
     */
    private String patrolChargeId;

    public AttPustBase() {
    }

}

