package com.ygkj.lcsyn.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BhLgMfu {

    @TableId(type = IdType.INPUT)
    /**
     * id
     */
    private Integer id;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 年度维修养护计划
     */
    private String mtnPlan;

    /**
     * 年度维修养护计划预算金额
     */
    private Integer budgetamount;

    /**
     * 上级下达的年度维修养护经费
     */
    private Integer superiorfunds;

    /**
     * 自筹的年度维修养护经费
     */
    private Integer selffunds;

    /**
     * 已使用的维修养护资金
     */
    private BigDecimal usedamount;

    /**
     * 已支付的维修养护资金
     */
    private BigDecimal paidamount;

    /**
     * 工程编号
     */
    private String pid;

    /**
     * 年度维修养护事项1名称
     */
    private String name;

    /**
     * 年度维修养护事项1计划开始时间
     */
    private Date stTm;

    /**
     * 年度维修养护事项1计划完成时间
     */
    private Date compTm;

    /**
     * 年度维修养护事项1养护前照片
     */
    private String bfpic;

    /**
     * 年度维修养护事项1养护后照片
     */
    private String afpic;

    /**
     * 实际完成时间
     */
    private Date completeTime;

    /**
     * uploadprojectmaintenanceitem上报状态
     */
    private Integer isUploadSuccess;

    /**
     * uploadmaintenancecomplete上报状态
     */
    private Integer isUploadSuccessComplete;

    /**
     * uploadprojectmaintenanceitemuploadmaintenancecomplete报错
     */
    private String uploadErrormsg;

    /**
     * uploadprojectmaintenanceitem返回编码
     */
    private String uploadKey;

    /**
     * 隐患记录编号
     */
    private Integer objId;

    /**
     * 维养合同
     */
    private String mfuContract;

    /**
     * 维养合同验收记录
     */
    private String mfuAcceptRecord;

    /**
     * 维养合同验收时间
     */
    private Date mfuAcceptTime;

    /**
     * 生成维养记录表
     */
    private Integer mfuAid;

    /**
     * 维修养护人员
     */
    private Integer mfuUid;

    /**
     * 维修养护情况记录
     */
    private String mfuStatusRecord;

    /**
     * 维修养护情况记录时间
     */
    private Date mfuStatusTime;

    /**
     * 上报时间
     */
    private Date uploadTm;

    /**
     * 维修养护要求
     */
    private String mfuRequired;

    /**
     * 类型（sign=4150）
     */
    private String mfType;

    /**
     * 部位
     */
    private String position;

    /**
     * 人员
     */
    private String person;

    /**
     * 巡查路线
     */
    private String patrolRange;

    /**
     * 验收记录附件
     */
    private String mfuAcceptAid;
}
