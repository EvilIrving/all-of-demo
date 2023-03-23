package com.ygkj.gragh.model;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 农村供水工程基础信息表
 * @date 2021-04-22
 */
@Data
public class AttCwsBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 农村供水工程代码
     */
    private String cwsCode;

    /**
     * 农村供水工程名称
     */
    private String cwsName;

    /**
     * 农村供水工程经度
     */
    private BigDecimal cwsLong;

    /**
     * 农村供水工维度
     */
    private BigDecimal cwsLat;

    /**
     * 农村供水工程所在位置
     */
    private String cwsLoc;

    /**
     * 工程类型
     */
    private String engType;

    /**
     * 供水方式
     */
    private String wasuType;

    /**
     * 供水范围
     */
    private String wasuRang;

    /**
     * 设计供水规模
     */
    private Long desWasuScal;

    /**
     * 设计供水入口
     */
    private BigDecimal desWasuPop;

    /**
     * 工程建设情况
     */
    private String engStat;

    /**
     * 水处理工艺
     */
    private String wtPro;

    /**
     * 消毒设施设备
     */
    private String dfType;

    /**
     * 开工时间
     */
    private Date startDate;

    /**
     * 竣工验收时间
     */
    private Date compDate;

    /**
     * 是否通过标准化管理创建
     */
    private String staMan;

    /**
     * 水源类型
     */
    private String watersupplytype;

    /**
     * 地区编码
     */
    private String citycode;

    /**
     * 水源地id
     */
    private String watersourceid;

    /**
     * 水厂id
     */
    private String waterworksid;

    /**
     * 供水村庄
     */
    private String watervillages;

    /**
     * 建成年份
     */
    private Integer duilderyear;

    /**
     * 建成月份
     */
    private Integer buildermonth;

    /**
     * 净水处理方式
     */
    private String watercleantype;

    /**
     * 供水检测类型
     */
    private String watersupplytesttype;

    /**
     * 输配水管网图
     */
    private String waterpipepic;

    /**
     * 管理单位名称
     */
    private String managerunitname;

    /**
     * 管理单位人员数量
     */
    private Integer managerunitcount;

    /**
     * 管理主体类型
     */
    private String managermaintype;

    /**
     * 农村居民人口(人)
     */
    private Integer ruralResidentPopulation;

    /**
     * 供水保证率(%)
     */
    private BigDecimal supplyGuaranteedRate;

    /**
     * 供水水压合格率
     */
    private BigDecimal waterPressureQualifiedRate;

    /**
     * 管网漏损率
     */
    private BigDecimal leakBreakRate;

    /**
     * 设备完好率
     */
    private BigDecimal facilityGoodRate;

    /**
     * 管网修漏及时率
     */
    private BigDecimal repairInTimeRate;

    /**
     * 抄表到户率
     */
    private BigDecimal readingCheckRate;

    /**
     * 年实际供水总量(万 吨)
     */
    private BigDecimal yearlyTotalWaterSupply;

    /**
     * 供水状态
     */
    private String supplyStatus;

    /**
     * 备注
     */
    private String note;

    /**
     * 记录生效时间
     */
    private Date effDate;

    /**
     * 记录生效时间
     */
    private Date exprDate;

    /**
     * 分档类别
     */
    private String classification;

    /**
     * 河流名称
     */
    private String riverName;

    /**
     * 工程类别
     */
    private String projectCategory;

    /**
     * 规模（m3/d）
     */
    private String scale;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 负责人电话
     */
    private String phone;

    /**
     * 负责人姓名
     */
    private String chargeName;

    /**
     * 供水站图片
     */
    private String projectImg;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private Integer delFlag;

    public AttCwsBase() {
    }

}
