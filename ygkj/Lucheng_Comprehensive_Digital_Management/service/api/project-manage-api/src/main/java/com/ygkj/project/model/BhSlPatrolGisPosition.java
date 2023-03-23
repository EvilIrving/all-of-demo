package com.ygkj.project.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description 工程巡查点巡查记录表
 * @author
 * @date 2022-04-27
 */
@Data
@ApiModel("工程巡查点巡查记录表")
public class BhSlPatrolGisPosition implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 工程编号
     */
    @ApiModelProperty("工程编号")
    private String skid;

    /**
     * 巡查总记录编号
     */
    @ApiModelProperty("巡查总记录编号")
    private Integer gid;

    /**
     * 巡查点名称
     */
    @ApiModelProperty("巡查点名称")
    private String projectPosition;

    /**
     * 巡查人员编号
     */
    @ApiModelProperty("巡查人员编号")
    private Integer userrid;

    /**
     * 巡查上报时间
     */
    @ApiModelProperty("巡查上报时间")
    private Date patrolTm;

    /**
     * 是否安全
     */
    @ApiModelProperty("是否安全")
    private Integer issafe;

    /**
     * 隐患详情
     */
    @ApiModelProperty("隐患详情")
    private String hiddenTrouble;

    /**
     * 巡查审核情况（弃）
     */
    @ApiModelProperty("巡查审核情况（弃）")
    private String hiddenTroublePropose;

    /**
     * 隐患状态（未处理、处理中、已完成）
     */
    @ApiModelProperty("隐患状态（未处理、处理中、已完成）")
    private Integer hiddenTroubleStatus;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private BigDecimal lgtd;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private BigDecimal lttd;

    /**
     * 校核时间
     */
    @ApiModelProperty("校核时间")
    private Date proposeTm;

    /**
     * 隐患处理前附件编号，多个逗号隔开
     */
    @ApiModelProperty("隐患处理前附件编号，多个逗号隔开")
    private String beforePic;

    /**
     * 隐患处理后附件编号，多个逗号隔开
     */
    @ApiModelProperty("隐患处理后附件编号，多个逗号隔开")
    private String afterPic;

    /**
     * 审核时间
     */
    @ApiModelProperty("审核时间")
    private Date verifyTime;

    /**
     * 隐患指派处理人员
     */
    @ApiModelProperty("隐患指派处理人员")
    private String assignUid;

    /**
     * 安全检查点描述编号
     */
    @ApiModelProperty("安全检查点描述编号")
    private String checkpointSaveId;

    /**
     * 隐患检查点描述编号
     */
    @ApiModelProperty("隐患检查点描述编号")
    private String checkpointHiddenId;

    /**
     * 巡查点编号
     */
    @ApiModelProperty("巡查点编号")
    private Integer cellId;

    /**
     * 巡查类型
     */
    @ApiModelProperty("巡查类型")
    private Integer patrolType;

    /**
     * 数据来源 1-标化 2-本地新增
     */
    @ApiModelProperty("数据来源 1-标化 2-本地新增")
    private Integer sourceType;

    public BhSlPatrolGisPosition() {
    }

}

