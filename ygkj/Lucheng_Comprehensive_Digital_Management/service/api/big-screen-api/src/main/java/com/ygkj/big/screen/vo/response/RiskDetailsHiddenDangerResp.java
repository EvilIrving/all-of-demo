package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/16 17:26
 */
@Data
@ApiModel("隐患点详情回参")
public class RiskDetailsHiddenDangerResp implements Serializable {

    /**
     * 工程名称
     */
    @ApiModelProperty("工程名称")
    private String projName;

    /**
     * 工程类型
     */
    @ApiModelProperty("工程类型")
    private String projType;

    /**
     * 检查性质:工程自查 县级检查 市级抽查 省级督查
     */
    @ApiModelProperty("发现渠道")
    private String property;

    /**
     * 流域
     */
    @ApiModelProperty("流域")
    private String riverBasin;

    /**
     * 行政区划
     */
    @ApiModelProperty("行政区划")
    private String adminDivision;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private String lng;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private String lat;

    /**
     * 详细地址
     */
    @ApiModelProperty("位置描述")
    private String address;

    /**
     * 主要工程量
     */
    @ApiModelProperty("主要工程量")
    private String quantities;

    /**
     * 上报人员
     */
    @ApiModelProperty("上报人员")
    private String reportPerson;

    /**
     * 审核人员名称
     */
    @ApiModelProperty("审核人员名称")
    private String checkPerson;

    /**
     * 上报时间
     */
    @ApiModelProperty("发现时间")
    private String reportTime;

    /**
     * 隐患类型标志 252为高风险点
     */
    @ApiModelProperty(" 隐患类型标志 252为高风险点")
    private String sbSign;

    /**
     * 整改情况
     */
    @ApiModelProperty("整改情况")
    private String recCondition;

    /**
     * 隐患类型：防汛准备、工程安全、检测预警、其他
     */
    @ApiModelProperty("隐患类型")
    private String dangerType;

    /**
     * 风险描述
     */
    @ApiModelProperty("风险描述")
    private String dangerDes;

    /**
     * 整改责任单位
     */
    @ApiModelProperty("整改责任单位")
    private String dutyUnit;

    /**
     * (整改)责任人
     */
    @ApiModelProperty(" (整改)责任人")
    private String dutyPerson;

    /**
     * (整改)责任电话
     */
    @ApiModelProperty("(整改)联系电话")
    private String dutyPhone;

    /**
     * 主管/监管单位
     */
    @ApiModelProperty("主管/监管单位")
    private String regulators;

    /**
     * 主管/监管联系人
     */
    @ApiModelProperty("主管/监管联系人")
    private String regulPerson;

    /**
     * 主管/监管联系电话
     */
    @ApiModelProperty("主管/监管联系电话")
    private String regulPhone;

    private String fileInfoListSb;

    /**
     * 上报时无图说明
     */
    @ApiModelProperty("图片/无图说明")
    private String sbpictext;

    @ApiModelProperty("制定整改计划")
    private MakeRectificationPlan makeRectificationPlan;

    //整改计划文件str
    private String recPlanFile;

    @ApiModelProperty("文件")
    private List<FileDetail> fileInfoList;

    //整改项目str
    private String sbHideDangerItems;
    //整改节点1
    @ApiModelProperty("整改节点")
    private RectificationNodeResp rectificationNodeResp;

    /**
     * 整改完成相关图片str
     */
    private String recCompleteFile;

    @ApiModelProperty("整改完成")
    private RectificationCompletedResp rectificationCompletedResp;
}
