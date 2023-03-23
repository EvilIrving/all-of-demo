package com.ygkj.river.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 任务巡河-任务表
 * @date 2021-04-06
 */
@Data
@ApiModel("任务巡河-任务Model")
public class RiverPatrolTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    @ApiModelProperty("任务id")
    private String id;

    /**
     * 任务名称
     */
    @ApiModelProperty("任务名称")
    private String taskName;

    /**
     * 河流代码
     */
    @ApiModelProperty("河流代码")
    private String riverCode;

    /**
     * 河流名称
     */
    @ApiModelProperty("河流名称")
    private String riverName;

    /**
     * 河道等级
     */
    @ApiModelProperty("河道等级")
    private String riverLevel;

    /**
     * 所属流域
     */
    @ApiModelProperty("所属流域")
    private String riverBas;

    /**
     * 河长级别
     */
    @ApiModelProperty("河长级别")
    private String riverManagerGrade;

    /**
     * 河长名称
     */
    @ApiModelProperty("河长名称")
    private String riverManageName;

    /**
     * 河长联系方式
     */
    @ApiModelProperty("河长联系方式")
    private String riverManagePhone;

    /**
     * 摄像头代码，多个摄像头用逗号隔开
     */
    @ApiModelProperty("摄像头代码，多个摄像头用逗号隔开")
    private String cameraCode;

    /**
     * 摄像头名称，多个摄像头名称用逗号隔开
     */
    @ApiModelProperty("摄像头名称，多个摄像头名称用逗号隔开")
    private String cameraName;

    /**
     * 巡河频次：1小时1次；2小时1次；3小时1次；6小时1次；12小时1次；（数据字典）
     */
    @ApiModelProperty("巡河频次：1小时1次；2小时1次；3小时1次；6小时1次；12小时1次；（数据字典）")
    private String patrolFreeq;

    /**
     * 行政区划代码
     */
    @ApiModelProperty("行政区划代码")
    private String adcd;

    /**
     * 行政区划名称
     */
    @ApiModelProperty("行政区划名称")
    private String adnm;

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
    private int delFlag;

    /**
     * 部门id
     */
    private String deptId;

    public RiverPatrolTask() {
    }

}
