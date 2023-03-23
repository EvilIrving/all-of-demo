package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/17 10:36
 */
@Data
@ApiModel("风险-高风险点详情回参")
public class RiskDetailsHighriskResp implements Serializable {


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
     * 发现渠道（xxjc为县级检查；zczc为工程自查；sjcc为市级抽查；sjdc为省级督查）
     */
    @ApiModelProperty("发现渠道（xxjc为县级检查；zczc为工程自查；sjcc为市级抽查；sjdc为省级督查）")
    private String property;

    /**
     * 流域
     */
    @ApiModelProperty("流域")
    private String riverBasin;


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
     * 位置描述
     */
    @ApiModelProperty("位置描述")
    private String address;

    /**
     * 上报人姓名
     */
    @ApiModelProperty("上报人员")
    private String reportPerson;

    /**
     * 审核人员
     */
    @ApiModelProperty("审核人员")
    private String checkPerson;

    /**
     * 上报时间
     */
    @ApiModelProperty("发现时间")
    private String reportTime;

    /**
     * 隐患类型标志 252为高风险点
     */
    @ApiModelProperty("风险类型 252为高风险点")
    private String sbSign;

    /**
     * 整改情况
     */
    @ApiModelProperty("整改情况")
    private String recCondition;

    /**
     * 风险标示:：1为红 2为橙 3为黄
     */
    @ApiModelProperty("风险标示:：1为红 2为橙 3为黄")
    private String colorGrade;

    /**
     * 风险描述
     */
    @ApiModelProperty("风险描述")
    private String dangerDes;

    /**
     * 管控措施
     */
    @ApiModelProperty("管控措施")
    private String riskPlan;

    /**
     * 整改责任单位
     */
    @ApiModelProperty("整改责任单位")
    private String dutyUnit;

    /**
     * 责任人
     */
    @ApiModelProperty("责任人")
    private String dutyPerson;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private String dutyPhone;

    /**
     * 无图说明
     */
    @ApiModelProperty("图片/无图说明")
    private String cancelPicText;

    /**
     * 整改完成时间
     */
    @ApiModelProperty("整改完成时间")
    private String recCompleteDate;

    /**
     * 整改成果描述
     */
    @ApiModelProperty("整改成果描述")
    private String recCompleteDescribe;

    //文件
    private String fileInfoListSb;

    @ApiModelProperty("文件")
    private List<FileDetail> fileDetails;

    //整改成果相关文件
    private String recCompleteFile;

    @ApiModelProperty("销号上传文件")
    private List<FileDetail> numberUploadFiles;

    @ApiModelProperty("销号上传文件")
    private List<FileDetail> numberUploadImages;
}
