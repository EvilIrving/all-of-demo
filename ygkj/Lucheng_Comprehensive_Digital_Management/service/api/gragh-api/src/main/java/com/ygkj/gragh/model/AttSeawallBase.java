package com.ygkj.gragh.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Huangzh
 * @description 海塘基础信息表
 * @date 2020-12-09
 */
@Data
@ToString
public class AttSeawallBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 海塘编码
     */
    @TableId("seawall_code")
    private String seawallCode;

    /**
     * 海塘名称
     */
    private String seawallName;

    /**
     * 海塘型式
     */
    private String seawallType;

    /**
     * 海塘级别
     */
    private String seawallLevel;

    /**
     * 代表潮位站代 码
     */
    private String tideStationCode;

    /**
     * 工程竣工验收 情况
     */
    private String projectCompletion;

    /**
     * 开工时间（非加 固）
     */
    private String startDate;

    /**
     * 竣工验收时间 （非加固）
     */
    private String completionDate;

    /**
     * 海塘长度
     */
    private BigDecimal seawallLength;

    /**
     * 起点位置
     */
    private String startLocation;

    /**
     * 起点桩号
     */
    private Integer startPileNo;

    /**
     * 海堤（段）起点 纬度
     */
    private BigDecimal seawallStartLatitude;

    /**
     * 海堤（段）起点 经度
     */
    private BigDecimal seawallStartLongitude;

    /**
     * 终点位置
     */
    private String finalLocation;

    /**
     * 终点桩号
     */
    private Integer finalPileNo;

    /**
     * 海堤（段）终点 纬度
     */
    private BigDecimal seawallFinalLatitude;

    /**
     * 海堤（段）终点 经度
     */
    private BigDecimal seawallFinalLongitude;

    /**
     * 起点堤顶高程
     */
    private BigDecimal startCrestAltitude;

    /**
     * 终点堤顶高程
     */
    private BigDecimal finalCrestAltitude;

    /**
     * 设计潮位
     */
    private BigDecimal designTiddeStandard;

    /**
     * 实际防潮标准
     */
    private String preventTideStandard;

    /**
     * 水闸数量
     */
    private Integer sluiceCount;

    /**
     * 保护人口
     */
    private String protectionPopulation;

    /**
     * 保护重点设施
     */
    private String protectionKeyFacility;

    /**
     * 代表潮位站名 称
     */
    private String tideStationName;

    /**
     * 参照潮位站蓝 色警戒潮位(米)
     */
    private BigDecimal tideStationBlueLevel;

    /**
     * 参照潮位站黄 色警戒潮位(米)
     */
    private BigDecimal tideStationYellowLevel;

    /**
     * 参照潮位站橙 色警戒潮位(米)
     */
    private BigDecimal tideStationOrangeLevel;

    /**
     * 参照潮位站红 色警戒潮位(米)
     */
    private BigDecimal tideStationRedLevel;

    /**
     * 完工时间（非加 固）
     */
    private String finishTime;

    /**
     * 达到规划防潮 标准的长度
     */
    private BigDecimal reachStandardLength;

    /**
     * 设计防潮标准
     */
    private Double designTideStandard;

    /**
     * 是否竣工验收 （非加固）
     */
    private String isComplete;

    /**
     * 海塘平均高程 （米）
     */
    private BigDecimal averageAltitude;

    /**
     * 保护面积（平方 公里）
     */
    private BigDecimal protectionArea;

    /**
     * 所在村名称
     */
    private String village;

    /**
     * 更新时间
     */
    private Date modifyTime;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

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
     * 线
     */
    private String points;

    /**
     * 技术责任人id，关联到用户体系
     */
    private String technologyChargeId;
    /**
     * 巡查责任人id，关联到用户体系
     */
    private String patrolChargeId;

    /**
     * 海塘走线
     */
    private String coorpot;

    /**
     * 行政区划编码
     */
    private String areaCode;

    /**
     * 所属流域
     */
    private String bas;

    /**
     * 所属岸段
     */
    private String coastalCode;

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

    private String areaName;

    /**
     * 是否病险工程
     */
    private Boolean dilapProj;

    /**
     * 塘顶宽度（最小值m）
     */
    private String minWidth;

    /**
     * 塘顶宽度（最大值m）
     */
    private String maxWidth;

    /**
     * 最低高程
     */
    private Double lowestElevation;

    /**
     * 海塘高度（最小值m）
     */
    private String minHeight;

    /**
     * 海塘高度（最大值m）
     */
    private String maxHeight;

    /**
     * 海塘类型
     */
    private String seawallPattern;

    /**
     * 水准基面
     */
    private String levelDatum;

    public AttSeawallBase() {
    }

    public Boolean judgeDilapProj() {
        if (safetyConclusion != null && !"".equals(safetyConclusion.trim())) {
            // 安全鉴定结果只要不是一类的，都是病险
            dilapProj = !safetyConclusion.contains("一类");
        }
        return dilapProj;
    }

}

