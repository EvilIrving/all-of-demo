package com.ygkj.flood.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Huangzh
 * @description 水库基础信息表
 * @date 2020-12-09
 */
@Data
public class AttResBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 水库代码
     */
    private String resCode;

    /**
     * 水库名称
     */
    private String resName;

    /**
     * 左下角经度
     */
    private BigDecimal lowLeftLong;

    /**
     * 左下角维度
     */
    private BigDecimal lowLeftLat;

    /**
     * 右上角经度
     */
    private BigDecimal upLeftLon;

    /**
     * 右上角维度
     */
    private BigDecimal upRightLat;

    /**
     * 水库所在位置
     */
    private String resLoc;

    /**
     * 水库类型
     */
    private String resType;

    /**
     * 工程登别
     */
    private String engGrad;

    /**
     * 工程规模
     */
    private String engScal;

    /**
     * 坝址控制流域面积
     */
    private BigDecimal watShedArea;

    /**
     * 坝址多年平均径流量
     */
    private BigDecimal daadMulAverRuof;

    /**
     * 防洪高水位
     */
    private BigDecimal uppLevFlco;

    /**
     * 正常蓄水位
     */
    private BigDecimal normWatLev;

    /**
     * 正常蓄水位相应水面面积
     */
    private BigDecimal normPoolStagArea;

    /**
     * 正常蓄水位相应库容
     */
    private BigDecimal normPoolStagCap;

    /**
     * 主汛期防洪限制水位
     */
    private BigDecimal flLowLimLev;

    /**
     * 防汛限制水位库容
     */
    private BigDecimal flLowLimLevCap;

    /**
     * 死水位
     */
    private BigDecimal deadLev;

    /**
     * 总库容
     */
    private BigDecimal totCap;

    /**
     * 兴利库容
     */
    private BigDecimal benResCap;

    /**
     * 死库容
     */
    private BigDecimal deadLcap;

    /**
     * 调洪库容
     */
    private BigDecimal storFlCap;

    /**
     * 防洪库容
     */
    private BigDecimal flcoLcap;

    /**
     * 水库下游防洪控制断面
     */
    private String resdmSec;

    /**
     * 河道安全过流能力
     */
    private BigDecimal risaCap;

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
    private Date scompDate;

    /**
     * 归口管理部门
     */
    private String admDep;

    /**
     * 主要挡水建筑物类型
     */
    private String mainWrType;

    /**
     * 主要挡水坝类型
     */
    private String damType;

    /**
     * 主要泄洪建筑物形式
     */
    private String mainFlType;

    /**
     * 主坝坝高
     */
    private BigDecimal damSizeHig;

    /**
     * 主坝坝长
     */
    private BigDecimal damSizeLen;

    /**
     * 最大泄洪流量
     */
    private BigDecimal maxDisFlow;

    /**
     * 设计洪水位
     */
    private BigDecimal desFlStag;

    /**
     * 溢洪道进口宽度(米)
     */
    private BigDecimal spillwayImportWidth;

    /**
     * 放水设施形式
     */
    private String drainageStructure;

    /**
     * 是否正在开展除险加固
     */
    private String isReinforce;

    /**
     * 完工时间（非加固）
     */
    private String finishTime;

    /**
     * 主要功能
     */
    private String function;

    /**
     * 高程体系
     */
    private String elevationSytem;

    /**
     * 设计洪水标准(年)
     */
    private Integer designFloodStandard;

    /**
     * 校核洪水标准(年)
     */
    private Integer checkFloodStandard;

    /**
     * 校核洪水位(米)
     */
    private BigDecimal checkFloodLevel;

    /**
     * 梅汛期限制水位(米)
     */
    private BigDecimal meiFloodLimitWaterLevel;

    /**
     * 台汛期限制水位(米)
     */
    private BigDecimal yphoonFloodLlimitWaterLevel;

    /**
     * 历史最高水位(米)
     */
    private BigDecimal historyHighestWaterLevel;

    /**
     * 历史最高水位发生日期 (年/月/日)
     */
    private String historyHighestWaterLevelDate;

    /**
     * 主坝坝体防渗形式
     */
    private String damImperviousnessType;

    /**
     * 副坝座数(座)
     */
    private Integer auxiliaryDamCount;

    /**
     * 正常溢洪道型
     */
    private String normalSpillwayType;

    /**
     * 正常溢洪道堰顶高程 (米)
     */
    private BigDecimal normalSpillwayCrestElevation;

    /**
     * 正常溢洪道堰顶净宽 (米)
     */
    private BigDecimal normalSpillwayCrestWidth;

    /**
     * 正常溢洪道最大泄量(立 方米/秒)
     */
    private BigDecimal normalSpillwayMaxOutflow;

    /**
     * 非常溢洪道型式
     */
    private String specialSpillwayType;

    /**
     * 非常溢洪道堰顶高程 (米)
     */
    private BigDecimal specialSpillwayCrestElevation;

    /**
     * 泄洪洞形式
     */
    private String floodTunnelType;

    /**
     * 泄洪洞进口底高程(米)
     */
    private BigDecimal floodTunnelImportElevation;

    /**
     * 泄洪洞最大泄量(立方米 /秒)
     */
    private BigDecimal floodTunnelMaxOutflow;

    /**
     * 输水洞形式
     */
    private String conveyanceTunnelType;

    /**
     * 输水洞进口底高程(米)
     */
    private BigDecimal conveyanceTunnelImportElevation;

    /**
     * 输水洞最大流量(立方米 /秒)
     */
    private BigDecimal conveyanceTunnelMaxFlow;

    /**
     * 设计灌溉面积(万亩)
     */
    private BigDecimal designIrrigationArea;

    /**
     * 实际灌溉面积(万亩)
     */
    private BigDecimal actualIrrigationArea;

    /**
     * 重要保护对象
     */
    private String importantProtectObject;

    /**
     * 库区涉及的县（市、区） （有多个的，用“、”分 开）
     */
    private String relatedCounties;

    /**
     * 最近一次除险加固完工 时间（年/月）
     */
    private String lastReinforceTime;

    /**
     * 最近一次除险加固竣工 时间（年/月）
     */
    private String lastReinforceCompleteTime;

    /**
     * 闸门孔数(个)
     */
    private Integer sluiceGateCount;

    /**
     * 闸门编号
     */
    private String sluiceGateNames;

    /**
     * 是否有备用电源
     */
    private String hasEmergencyPower;

    /**
     * 是否有闸门
     */
    private String hasSluiceGate;

    /**
     * 是否竣工验收（非加固）
     */
    private String isComplete;

    /**
     * 水库面积曲线
     */
    private String rac;

    /**
     * 水库库容曲线
     */
    private String rvc;

    /**
     * 备案登记号
     */
    private String rscd;

    /**
     * 备用电源数量
     */
    private Integer isPow;

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

    /**
     * 行政区划代码
     */
    private String areaCode;

    /**
     * 纳蓄能力
     */
    private Double storageAbility;
    /**
     * 实时水位
     */
    private Double waterLevel;
    /**
     * 剩余库容
     */
    private BigDecimal leftCapacity;

    public AttResBase() {
    }

}