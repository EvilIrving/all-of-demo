package com.ygkj.big.screen.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 风险-隐患点详情表
 * @date 2021-06-16
 */
@Data
public class RiskDetailsHiddendanger implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 钱塘江平台的工程id
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
     * 检查性质:工程自查 县级检查 市级抽查 省级督查
     */
    private String property;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 主要工程量
     */
    private String quantities;

    /**
     * 上报人员
     */
    private String reportPerson;

    /**
     * 上报时间
     */
    private String reportTime;

    /**
     * 审核人员名称
     */
    private String checkPerson;

    /**
     * 隐患类型：防汛准备、工程安全、检测预警、其他
     */
    private String dangerType;

    /**
     * 整改情况
     */
    private String recCondition;

    /**
     * 隐患类型标志 252为高风险点
     */
    private String sbSign;

    /**
     * 风险描述
     */
    private String dangerDes;

    /**
     * 整改责任单位
     */
    private String dutyUnit;

    /**
     * (整改)责任人
     */
    private String dutyPerson;

    /**
     * (整改)责任电话
     */
    private String dutyPhone;

    /**
     * 主管/监管单位
     */
    private String regulators;

    /**
     * 主管/监管单位联系人
     */
    private String regulPerson;

    /**
     * 主管/监管单位联系电话
     */
    private String regulPhone;

    /**
     * 文件str
     */
    private String fileinfoListSb;

    /**
     * 上报时无图说明
     */
    private String sbpictext;

    /**
     * 是否为草稿 0.否，1.是，2.已消号
     */
    private String scraps;

    /**
     * 流域名称
     */
    private String riverBasin;

    /**
     * 市
     */
    private String city;

    /**
     * 县
     */
    private String county;

    /**
     * 整改期限
     */
    private String recExpirationDate;

    /**
     * 整改计划描述
     */
    private String recPlanDescribe;

    /**
     * 计划修编预案方案（0.未制定，1.已制定）
     */
    private String programmePlan;

    /**
     * 管控措施
     */
    private String recSuperviseMeasures;

    /**
     * 落实情况
     */
    private String recImplementSituation;

    /**
     * 整改计划相关图片
     */
    private String recplanfile;

    /**
     * 整改节点1rn整改节点1rn整改节点1rn整改节点1rn整改节点1str
     */
    private String sbHideDangerItems;

    /**
     * 整改成果描述
     */
    private String recCompleteDescribe;

    /**
     * 整改完成相关图片str
     */
    private String recCompleteFile;

    /**
     * 整改计划相关文件
     */
    private String recPlanFile;

    public RiskDetailsHiddendanger() {
    }
}