package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 水库基础信息表
 */
@Data
public class AttResBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("水库代码")
    private String resCode;


    @ApiModelProperty("水库名称")
    private String resName;


    @ApiModelProperty("左下角经度")
    private Double lowLeftLong;


    @ApiModelProperty("左下角维度")
    private Double lowLeftLat;


    @ApiModelProperty("右上角经度")
    private Double upLeftLon;


    @ApiModelProperty("右上角维度")
    private Double upRightLat;


    @ApiModelProperty("水库所在位置")
    private String resLoc;


    @ApiModelProperty("水库类型")
    private String resType;


    @ApiModelProperty("工程等别")
    private String engGrad;


    @ApiModelProperty("工程规模")
    private String engScal;


    @ApiModelProperty("坝址控制流域面积")
    private Double watShedArea;


    @ApiModelProperty("坝址多年平均径流量")
    private Double daadMulAverRuof;


    @ApiModelProperty("防洪高水位")
    private Double uppLevFlco;


    @ApiModelProperty("正常蓄水位")
    private Double normWatLev;


    @ApiModelProperty("正常蓄水位相应水面面积")
    private Double normPoolStagArea;


    @ApiModelProperty("正常蓄水位相应库容")
    private Double normPoolStagCap;


    @ApiModelProperty("主汛期防洪限制水位")
    private Double flLowLimLev;


    @ApiModelProperty("防汛限制水位库容")
    private Double flLowLimLevCap;


    @ApiModelProperty("死水位")
    private Double deadLev;


    @ApiModelProperty("总库容")
    private Double totCap;


    @ApiModelProperty("兴利库容")
    private Double benResCap;


    @ApiModelProperty("死库容")
    private Double deadCap;


    @ApiModelProperty("调洪库容")
    private Double storFlCap;


    @ApiModelProperty("防洪库容")
    private Double flcoCap;


    @ApiModelProperty("水库下游防洪控制断面")
    private String resdmSec;


    @ApiModelProperty("河道安全过流能力")
    private Double risaCap;


    @ApiModelProperty("工程建设情况")
    private String engStat;


    @ApiModelProperty("开工时间")
    private String startDate;


    @ApiModelProperty("竣工验收时间")
    private String compDate;


    @ApiModelProperty("归口管理部门")
    private String admDep;


    @ApiModelProperty("主要挡水建筑物类型")
    private String mainWrType;


    @ApiModelProperty("主要挡水坝类型")
    private String damType;


    @ApiModelProperty("主要泄洪建筑物形式")
    private String mainFlType;


    @ApiModelProperty("主坝坝高")
    private Double damSizeHig;


    @ApiModelProperty("主坝坝长")
    private Double damSizeLen;


    @ApiModelProperty("最大泄洪流量")
    private Double maxDisFlow;


    @ApiModelProperty("设计洪水位")
    private Double desFlStag;


    @ApiModelProperty("溢洪道进口宽度(米)")
    private Double spillwayImportWidth;


    @ApiModelProperty("放水设施形式")
    private String drainageStructure;


    @ApiModelProperty("是否正在开展除险加固")
    private String isReinforce;


    @ApiModelProperty("完工时间（非加固）")
    private String finishTime;


    @ApiModelProperty("主要功能")
    private String function;


    @ApiModelProperty("高程体系")
    private String elevationSytem;


    @ApiModelProperty("设计洪水标准(年)")
    private Integer designFloodStandard;


    @ApiModelProperty("校核洪水标准(年)")
    private Integer checkFloodStandard;


    @ApiModelProperty("校核洪水位(米)")
    private Double checkFloodLevel;


    @ApiModelProperty("梅汛期限制水位(米)")
    private Double meiFloodLimitWaterLevel;


    @ApiModelProperty("台汛期限制水位(米)")
    private Double yphoonFloodLlimitWaterLevel;


    @ApiModelProperty("历史最高水位(米)")
    private Double historyHighestWaterLevel;


    @ApiModelProperty("历史最高水位发生日期 (年/月/日)")
    private String historyHighestWaterLevelDate;


    @ApiModelProperty("主坝坝体防渗形式")
    private String damImperviousnessType;


    @ApiModelProperty("副坝座数(座)")
    private Integer auxiliaryDamCount;


    @ApiModelProperty("正常溢洪道型")
    private String normalSpillwayType;


    @ApiModelProperty("正常溢洪道堰顶高程 (米)")
    private Double normalSpillwayCrestElevation;


    @ApiModelProperty("正常溢洪道堰顶净宽 (米)")
    private Double normalSpillwayCrestWidth;


    @ApiModelProperty("正常溢洪道最大泄量(立 方米/秒)")
    private Double normalSpillwayMaxOutflow;


    @ApiModelProperty("非常溢洪道型式")
    private String specialSpillwayType;


    @ApiModelProperty("非常溢洪道堰顶高程 (米)")
    private Double specialSpillwayCrestElevation;


    @ApiModelProperty("泄洪洞形式")
    private String floodTunnelType;


    @ApiModelProperty("泄洪洞进口底高程(米)")
    private Double floodTunnelImportElevation;


    @ApiModelProperty("泄洪洞最大泄量(立方米 /秒)")
    private Double floodTunnelMaxOutflow;


    @ApiModelProperty("输水洞形式")
    private String conveyanceTunnelType;


    @ApiModelProperty("输水洞进口底高程(米)")
    private Double conveyanceTunnelImportElevation;


    @ApiModelProperty("输水洞最大流量(立方米 /秒)")
    private Double conveyanceTunnelMaxFlow;


    @ApiModelProperty("设计灌溉面积(万亩)")
    private Double designIrrigationArea;


    @ApiModelProperty("实际灌溉面积(万亩)")
    private Double actualIrrigationArea;


    @ApiModelProperty("重要保护对象")
    private String importantProtectObject;


    @ApiModelProperty("库区涉及的县（市、区） （有多个的，用“、”分 开）")
    private String relatedCounties;


    @ApiModelProperty("最近一次除险加固完工 时间（年/月）")
    private String lastReinforceTime;


    @ApiModelProperty("最近一次除险加固竣工 时间（年/月）")
    private String lastReinforceCompleteTime;


    @ApiModelProperty("闸门孔数(个)")
    private Integer sluiceGateCount;


    @ApiModelProperty("闸门编号")
    private String sluiceGateNames;


    @ApiModelProperty("是否有备用电源")
    private String hasEmergencyPower;


    @ApiModelProperty("是否有闸门")
    private String hasSluiceGate;


    @ApiModelProperty("是否竣工验收（非加固）")
    private String isComplete;


    @ApiModelProperty("水库面积曲线")
    private String rac;


    @ApiModelProperty("水库库容曲线")
    private String rvc;


    @ApiModelProperty("备案登记号")
    private String rscd;


    @ApiModelProperty("备用电源数量")
    private Integer isPow;


    @ApiModelProperty("备注")
    private String note;


    @ApiModelProperty("记录生效时间")
    private String effDate;


    @ApiModelProperty("记录失效时间")
    private String exprDate;


    @ApiModelProperty("行政区划代码")
    private String areaCode;


    @ApiModelProperty("政府负责人用户id，关联到用户体系")
    private String govermentChargeId;


    @ApiModelProperty("水行政部门负责人用户id，关联到用户体系")
    private String waterDepartmentChargeId;


    @ApiModelProperty("主管部门负责人id，关联到用户体系")
    private String competentDepartmentChargeId;


    @ApiModelProperty("管理单位责任人id，关联到用户体系")
    private String manageDepartmentChargeId;


    @ApiModelProperty("技术责任人")
    private String technologyChargeId;


    @ApiModelProperty("巡查责任人")
    private String patrolChargeId;


    @ApiModelProperty("所属流域")
    private String bas;


    @ApiModelProperty("删除标记 0未删除 1已删除")
    private int delFlag;

    /**
     * 洪水预报系统对应工程id
     */
    @ApiModelProperty("洪水预报系统对应工程id")
    private String floodId;

    /**
     * 主测站编码
     */
    @ApiModelProperty("主测站编码")
    private String mainStcd;

    /**
     * 安全鉴定时间
     */
    @ApiModelProperty("安全鉴定时间")
    private String safetyTime;

    /**
     * 安全鉴定结论
     */
    @ApiModelProperty("安全鉴定结论")
    private String safetyConclusion;

    /**
     * 是否加固
     */
    @ApiModelProperty("是否加固")
    private String reinforce;
}
