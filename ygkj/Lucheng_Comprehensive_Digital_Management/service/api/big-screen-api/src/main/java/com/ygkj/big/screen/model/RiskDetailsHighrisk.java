package com.ygkj.big.screen.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 风险-高风险点详情表
 * @date 2021-06-17
 */
@Data
public class RiskDetailsHighrisk implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 钱塘江平台工程id
     */
    private String sbId;

    /**
     * 工程名称
     */
    private String projName;

    /**
     * 工程类型
     */
    private String projType;

    /**
     * 发现渠道（xxjc为县级检查；zczc为工程自查；sjcc为市级抽查；sjdc为省级督查）
     */
    private String property;

    /**
     * 城市
     */
    private String city;

    /**
     * 县区
     */
    private String county;

    /**
     * 位置描述
     */
    private String address;

    /**
     * 上报人姓名
     */
    private String reportPerson;

    /**
     * 审核人姓名
     */
    private String checkPerson;

    /**
     * 上报时间
     */
    private String reportTime;

    /**
     * 隐患类型标志 252为高风险点
     */
    private String sbSign;

    /**
     * 风险标示:：1为红 2为橙 3为黄
     */
    private String colorGrade;

    /**
     * 风险描述
     */
    private String dangerDes;

    /**
     * 管控措施
     */
    private String riskPlan;

    /**
     * 整改责任单位
     */
    private String dutyUnit;

    /**
     * 责任人
     */
    private String dutyPerson;

    /**
     * 联系电话
     */
    private String dutyPhone;

    /**
     * 文件
     */
    private String fileInfoListSb;

    /**
     * 无图说明
     */
    private String cancelPicText;

    /**
     * 整改完成时间
     */
    private String recCompleteDate;

    /**
     * 整改成果描述
     */
    private String recCompleteDescribe;

    /**
     * 整改成果相关文件
     */
    private String recCompleteFile;

    public RiskDetailsHighrisk() {
    }
}