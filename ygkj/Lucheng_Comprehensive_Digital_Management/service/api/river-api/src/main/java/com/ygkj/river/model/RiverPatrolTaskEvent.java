package com.ygkj.river.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 任务巡河事件表
 * @date 2021-04-06
 */
@Data
@ApiModel("任务巡河-事件")
public class RiverPatrolTaskEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件id
     */
    @ApiModelProperty("")
    private String id;

    /**
     * 任务id
     */
    @ApiModelProperty("")
    private String taskId;

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
     * 河流流域
     */
    @ApiModelProperty("河流流域")
    private String riverBas;

    /**
     * 巡河频次：1小时1次；2小时1次；3小时1次；6小时1次；12小时1次；（数据字典）
     */
    @ApiModelProperty("巡河频次")
    private String patrolFreeq;

    /**
     * 巡河方式：自动或手动
     */
    @ApiModelProperty("巡河方式")
    private String patrolMode;

    /**
     * 巡河时间
     */
    @ApiModelProperty("巡河时间")
    private Date patrolTime;

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

    public RiverPatrolTaskEvent() {
    }

}
