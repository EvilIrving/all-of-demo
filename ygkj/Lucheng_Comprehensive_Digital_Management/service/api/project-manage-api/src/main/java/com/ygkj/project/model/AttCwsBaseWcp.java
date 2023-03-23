package com.ygkj.project.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 农村供水工程基础信息表
 * @date 2021-08-21
 */
@Data
@ApiModel("农村供水工程基础信息表")
public class AttCwsBaseWcp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 农村供水工程代码
     */
    @ApiModelProperty("农村供水工程代码")
    private String cwsCode;

    /**
     * 农村供水工程名称
     */
    @ApiModelProperty("农村供水工程名称")
    private String cwsName;

    /**
     * 消毒工艺
     */
    @ApiModelProperty("消毒工艺")
    private String dfcProc;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String note;

    /**
     * have_ams
     */
    @ApiModelProperty("have_ams")
    private String haveAms;

    /**
     * have_ol_mon
     */
    @ApiModelProperty("have_ol_mon")
    private String haveOlMon;

    /**
     * 数据入库时间
     */
    @ApiModelProperty("数据入库时间")
    private Date tongTime;

    /**
     * wasu_rang_advilct
     */
    @ApiModelProperty("wasu_rang_advilct")
    private Integer wasuRangAdvilct;

    /**
     * 农村供水工程经度(°)
     */
    @ApiModelProperty("农村供水工程经度(°)")
    private BigDecimal cwsLong;

    /**
     * 涉水产品是否具有有效许可批件
     */
    @ApiModelProperty("涉水产品是否具有有效许可批件")
    private String haveVldApvl;

    /**
     * 自然村
     */
    @ApiModelProperty("自然村")
    private String naturalAdnm;

    /**
     * 记录失效时间
     */
    @ApiModelProperty("记录失效时间")
    private Date exprDate;

    /**
     * 处理工艺
     */
    @ApiModelProperty("处理工艺")
    private String treProc;

    /**
     * 供水执行水价
     */
    @ApiModelProperty("供水执行水价")
    private BigDecimal execPri;

    /**
     * 实际供水规模
     */
    @ApiModelProperty("实际供水规模")
    private BigDecimal actWasuScal;

    /**
     * 农村供水工程纬度(°)
     */
    @ApiModelProperty("农村供水工程纬度(°)")
    private BigDecimal cwsLat;

    /**
     * 厂区出入管理
     */
    @ApiModelProperty("厂区出入管理")
    private String haveEntrMgt;

    /**
     * 记录生效时间
     */
    @ApiModelProperty("记录生效时间")
    private Date effDate;

    /**
     * 管网总长度
     */
    @ApiModelProperty("管网总长度")
    private BigDecimal pipLgth;

    /**
     * 数据更新时间
     */
    @ApiModelProperty("数据更新时间")
    private Date modifyTime;

    /**
     * 行政村
     */
    @ApiModelProperty("行政村")
    private String adminAdnm;

    /**
     * 供应自然村村落数量
     */
    @ApiModelProperty("供应自然村村落数量")
    private Integer wasuRangVilct;

    /**
     * 运行成本水价
     */
    @ApiModelProperty("运行成本水价")
    private BigDecimal opPri;

    /**
     * 供管水人员是否有健康证
     */
    @ApiModelProperty("供管水人员是否有健康证")
    private String haveHealCert;

    /**
     * comple_time
     */
    @ApiModelProperty("comple_time")
    private String compleTime;

    /**
     * 设计供水规模(m3/d)
     */
    @ApiModelProperty("设计供水规模(m3/d)")
    private Integer desWasuScal;

    /**
     * 水厂照片(水厂效果图:pic_plant，主干管网图:pic_pipe，全貌图:pic_pano，消毒间:pic_disinfect，加药室:pic_dosing，水质检测室:pic_qualtest，清水池:pic_clean水源地:pic_source)
     */
    @ApiModelProperty("水厂照片(水厂效果图:pic_plant，主干管网图:pic_pipe，全貌图:pic_pano，消毒间:pic_disinfect，加药室:pic_dosing，水质检测室:pic_qualtest，清水池:pic_clean水源地:pic_source)")
    private String picFile;

    /**
     * low_inake_lvl
     */
    @ApiModelProperty("low_inake_lvl")
    private String lowInakeLvl;

    /**
     * have_ins_veo
     */
    @ApiModelProperty("have_ins_veo")
    private String haveInsVeo;

    /**
     * 工程规模
     */
    @ApiModelProperty("工程规模")
    private String engScal;

    /**
     * 供应自然村村落名称
     */
    @ApiModelProperty("供应自然村村落名称")
    private String wasuRangVilnm;

    /**
     * 工程类型
     */
    @ApiModelProperty("工程类型")
    private String engType;

    /**
     * 数源行政区划编码
     */
    @ApiModelProperty("数源行政区划编码")
    private String adcd;

    /**
     * 水质检测模式
     */
    @ApiModelProperty("水质检测模式")
    private String qualDfcMod;

    /**
     * 供管水人员是否进行卫生知识培训
     */
    @ApiModelProperty("供管水人员是否进行卫生知识培训")
    private String haveHygTrng;

    /**
     * 农村供水工程所在位置
     */
    @ApiModelProperty("农村供水工程所在位置")
    private String cwsLoc;

    /**
     * 设计供水人口(万人)
     */
    @ApiModelProperty("设计供水人口(万人)")
    private BigDecimal desWasuPop;

    /**
     * wasu_rang_advilnm
     */
    @ApiModelProperty("wasu_rang_advilnm")
    private String wasuRangAdvilnm;

    /**
     * reno_time
     */
    @ApiModelProperty("reno_time")
    private String renoTime;

    /**
     * have_wtol_mon
     */
    @ApiModelProperty("have_wtol_mon")
    private String haveWtolMon;

    /**
     * 是否制定突发应急预案
     */
    @ApiModelProperty("是否制定突发应急预案")
    private String haveEmerPla;

    /**
     * 全成本水价
     */
    @ApiModelProperty("全成本水价")
    private BigDecimal cstPri;

    /**
     * have_lab
     */
    @ApiModelProperty("have_lab")
    private String haveLab;

    /**
     * have_dfc_itg
     */
    @ApiModelProperty("have_dfc_itg")
    private String haveDfcItg;

    /**
     * have_auto_dfc
     */
    @ApiModelProperty("have_auto_dfc")
    private String haveAutoDfc;

    /**
     * 水厂是否有卫生许可证
     */
    @ApiModelProperty("水厂是否有卫生许可证")
    private String haveSanlic;

    /**
     * 供水方式
     */
    @ApiModelProperty("供水方式")
    private String wasuType;

    /**
     * 供水范围
     */
    @ApiModelProperty("供水范围")
    private String wasuRang;

    /**
     * 工程建设情况
     */
    @ApiModelProperty("工程建设情况")
    private String engStat;

    /**
     * 水处理工艺
     */
    @ApiModelProperty("水处理工艺")
    private String wtPro;

    /**
     * 消毒设施类型
     */
    @ApiModelProperty("消毒设施类型")
    private String dfType;

    /**
     * 是否通过标准化管理创建
     */
    @ApiModelProperty("是否通过标准化管理创建")
    private String staMan;

    /**
     * 水源类型
     */
    @ApiModelProperty("水源类型")
    private String watersupplytype;

    /**
     * 地区编号
     */
    @ApiModelProperty("地区编号")
    private String citycode;

    /**
     * 水源地id
     */
    @ApiModelProperty("水源地id")
    private String watersourceid;

    /**
     * 水厂id
     */
    @ApiModelProperty("水厂id")
    private String waterworksid;

    /**
     * 供水村庄
     */
    @ApiModelProperty("供水村庄")
    private String watervillages;

    /**
     * 建成年份
     */
    @ApiModelProperty("建成年份")
    private Integer builderyear;

    /**
     * 建成月份
     */
    @ApiModelProperty("建成月份")
    private Integer buildermonth;

    /**
     * 净水处理方式
     */
    @ApiModelProperty("净水处理方式")
    private String watercleantype;

    /**
     * 供水检测类型
     */
    @ApiModelProperty("供水检测类型")
    private String watersupplytesttype;

    /**
     * 输配水管网图
     */
    @ApiModelProperty("输配水管网图")
    private String waterpipepic;

    /**
     * 管理单位名称
     */
    @ApiModelProperty("管理单位名称")
    private String managerunitname;

    /**
     * 管理单位人员数量
     */
    @ApiModelProperty("管理单位人员数量")
    private Integer managerunitcount;

    /**
     * 管理主体类型
     */
    @ApiModelProperty("管理主体类型")
    private String managermaintype;

    /**
     * 农村居民人口(人)
     */
    @ApiModelProperty("农村居民人口(人)")
    private Integer ruralResidentPopulation;

    /**
     * 供水保证率(%)
     */
    @ApiModelProperty("供水保证率(%)")
    private BigDecimal supplyGuaranteedRate;

    /**
     * 供水水压合格率
     */
    @ApiModelProperty("供水水压合格率")
    private BigDecimal waterPressureQualifiedRate;

    /**
     * 管网漏损率
     */
    @ApiModelProperty("管网漏损率")
    private BigDecimal leakBreakRate;

    /**
     * 设备完好率
     */
    @ApiModelProperty("设备完好率")
    private BigDecimal facilityGoodRate;

    /**
     * 管网修漏及时率
     */
    @ApiModelProperty("管网修漏及时率")
    private BigDecimal repairInTimeRate;

    /**
     * 抄表到户率
     */
    @ApiModelProperty("抄表到户率")
    private BigDecimal readingCheckRate;

    /**
     * 年实际供水总量(万吨)
     */
    @ApiModelProperty("年实际供水总量(万吨)")
    private BigDecimal yearlyTotalWaterSupply;

    /**
     * 供水状态
     */
    @ApiModelProperty("供水状态")
    private String supplyStatus;

    /**
     * 开工时间
     */
    @ApiModelProperty("开工时间")
    private Date startDate;

    /**
     * 建成时间
     */
    @ApiModelProperty("建成时间")
    private Date compDate;

    /**
     * 操作标记
     */
    @ApiModelProperty("操作标记")
    private String op;

    /**
     * 所在乡镇（街道）名称
     */
    @ApiModelProperty("所在乡镇（街道）名称")
    private String cwsTown;

    /**
     * 供应行政村数量
     */
    @ApiModelProperty("供应行政村数量")
    private Integer wasuRangCt;

    public AttCwsBaseWcp() {
    }

}
