package com.ygkj.gragh.model;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Huangzh
 * @description 水闸基础信息表
 * @date 2020-12-09
 */
@Data
public class AttWagaBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 水闸代码
     */
    private String wagaCode;

    /**
     * 水闸名称
     */
    private String wagaName;

    /**
     * 起点经度
     */
    private BigDecimal startLong;

    /**
     * 起点维度
     */
    private BigDecimal startLat;

    /**
     * 终点经度
     */
    private BigDecimal endLong;

    /**
     * 终点维度
     */
    private BigDecimal endLat;

    /**
     * 水闸所在位置
     */
    private String wagaLoc;

    /**
     * 水闸类型
     */
    private String wagaType;

    /**
     * 水闸用途
     */
    private String wagaUse;

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
     * 设计最大过闸 流量
     */
    private BigDecimal desLockDisc;

    /**
     * 闸口数量
     */
    private Integer gaorNum;

    /**
     * 闸口总净宽
     */
    private BigDecimal gaorTotNetWid;

    /**
     * 孔口断面形式
     */
    private String op;

    /**
     * 孔口净高
     */
    private BigDecimal oh;

    /**
     * 孔口内径
     */
    private BigDecimal od;

    /**
     * 工程建设情况
     */
    private String engStat;

    /**
     * 开工时间
     */
    private String startDate;

    /**
     * 竣工验收时间
     */
    private String compDate;

    /**
     * 归口管理部门
     */
    private String amdDep;

    /**
     * 建成时间
     */
    private String builtdate;

    /**
     * 闸门宽度
     */
    private String gatewidth;

    /**
     * 闸底高度
     */
    private BigDecimal gatebottomelevation;

    /**
     * 里程
     */
    private Integer mileage;

    /**
     * 是否竣工验收
     */
    private String isComplete;

    /**
     * 是否为闸站工程
     */
    private String isGateStationProject;

    /**
     * 是否为套闸工程
     */
    private String isSetBrakeEngineering;

    /**
     * 闸上是否能通车
     */
    private String isTraffic;

    /**
     * 水闸参照水位站名称
     */
    private String referenceWlStationName;

    /**
     * 水闸参照水位站站号
     */
    private String referenceWlStationCode;

    /**
     * 水闸参照水位站警戒水位
     */
    private BigDecimal referenceWlStationWarningLevel;

    /**
     * 水闸参照水位站危急水位
     */
    private BigDecimal referenceWlStationCriticalLevel;

    /**
     * 高程体系
     */
    private String elevationSystem;

    /**
     * 闸门形式
     */
    private String gateType;

    /**
     * 闸门高度
     */
    private String gateSize;

    /**
     * 启闭机类型
     */
    private String hoistType;

    /**
     * 效能方式
     */
    private String energyDissipationType;

    /**
     * 节制闸过闸流量(立方米/秒)
     */
    private BigDecimal controlGateFlow;

    /**
     * 设计闸上水位(米)
     */
    private BigDecimal designSluiceUpstreamLevel;

    /**
     * 校核闸上水位(米)
     */
    private BigDecimal checkSluiceUpstreamLevel;

    /**
     * 上下游最大水位差(米)
     */
    private BigDecimal biggestWaterLevelDifferenc;

    /**
     * 正常蓄水位(米)
     */
    private BigDecimal normalImpoundageLevel;

    /**
     * 梅汛期内河控制水位(米)
     */
    private BigDecimal meiyufloodPeriodWaterLevel;

    /**
     * 台汛期内河控制水位(米)
     */
    private BigDecimal typhoonPeriodWaterLevel;

    /**
     * 属河道水闸或海塘水闸
     */
    private String isRiverSluice;

    /**
     * 是否五水共治任务
     */
    private String isWsgz;

    /**
     * 所在河道等级
     */
    private String riverLevel;

    /**
     * 所在河流岸别
     */
    private String bankSide;

    /**
     * 是否有备用电源
     */
    private String hasEmergencyPower;

    /**
     * 是否船闸工程
     */
    private String isShipLock;

    /**
     * 闸基处理方式
     */
    private String basePattern;

    /**
     * 静止模式
     */
    private String stillingPattern;

    /**
     * 设计洪水
     */
    private Integer designflood;

    /**
     * 校核洪水
     */
    private Integer checkflood;

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

    private String areaCode;

    private String bas;

    /**
     * 主测站编码，水闸本来没有主测站一说，但是目前数据库中有重复数据，一个测站可能关联多个工程。如果主测站为空，则查询att_st_base.proj_code
     */
    private String mainStcd;


    /**
     * 安全鉴定时间
     */
    private String safetyTime;

    /**
     * 安全鉴定结论
     */
    private String safetyConclusion;

    /**
     * 是否加固
     */
    private String reinforce;

    /**
     * 是否病险工程
     */
    private Boolean dilapProj;


    /**
     * 河道正常蓄水位
     */
    private BigDecimal riverNormalPoolLevel;

    /**
     * 死水位
     */
    private BigDecimal dealLevel;

    /**
     * 非汛期控制水位
     */
    private BigDecimal nonFloodPeriodWaterLevel;

    /**
     * 兴利控制水位
     */
    private BigDecimal hennessyPeriodWaterLevel;

    /**
     * 兴利死水位
     */
    private BigDecimal hennessyDealLevel;

    /**
     * 安全水位上限
     */
    private BigDecimal safeLevelUpper;

    /**
     * 安全水位下限
     */
    private BigDecimal safeLevelLower;

    public AttWagaBase() {
    }

    public Boolean judgeDilapProj() {
        if (safetyConclusion != null && !"".equals(safetyConclusion.trim())) {
            // 安全鉴定结果只要不是一类的，都是病险
            dilapProj = !safetyConclusion.contains("一类");
        }
        return dilapProj;
    }

}

