package com.ygkj.gragh.model;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Huangzh
 * @description 水电站基础信息表
 * @date 2020-12-09
 */
@Data
public class AttHystBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 水电站代码
     */
    private String hystCode;

    /**
     * 水电站名称
     */
    private String hystName;

    /**
     * 配套水库名称
     */
    private String sresName;

    /**
     * 配套水库库容
     */
    private BigDecimal sresCap;

    /**
     * 水库编码
     */
    private String reservoirCode;

    /**
     * 水电站经度
     */
    private BigDecimal hystLong;

    /**
     * 水电站纬度
     */
    private BigDecimal hystLat;

    /**
     * 水电站所在位置
     */
    private String hystLoc;

    /**
     * 水电站类型
     */
    private String hystType;

    /**
     * 总装机容量
     */
    private Integer totInsCap;

    /**
     * 保证出力
     */
    private Integer firmPow;

    /**
     * 额定水头
     */
    private BigDecimal ratHead;

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
    private String engStat;

    /**
     * 开工时间
     */
    private Date startDate;

    /**
     * 竣工验收时间
     */
    private Date compDate;

    /**
     * 归口管理部门
     */
    private String amdDep;

    /**
     * 机组台数(台)
     */
    private Integer generatorsCount;

    /**
     * 多年平均发电量 (万千瓦时)
     */
    private BigDecimal yearsAveragePowerGeneration;

    /**
     * 是否利用水库发电
     */
    private String isUseResvoirWater;

    /**
     * 本流域集雨面积 (平方千米)
     */
    private BigDecimal localWaterCollectionArea;

    /**
     * 水库安全技术认定日期
     */
    private Date reservoirSafetyIdentificationDate;

    /**
     * 水库安全技术认定结论
     */
    private String reservoirSafetyIdentificationConclusion;

    /**
     * 水轮机主要型式
     */
    private String hydraulicTurbineMainType;

    /**
     * 机组布置主要方式
     */
    private String generatorsArrangementMainWay;

    /**
     * 完工时间
     */
    private String completeTime;

    /**
     * 大坝类型按材料分
     */
    private String damTypeByMaterials;

    /**
     * 最大坝高(米)
     */
    private BigDecimal damMaxHeight;

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


    public AttHystBase() {
    }

}

