package com.ygkj.river.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * @description 项目举报表
 * @author lxl
 * @date 2022-07-12
 */
@Data
@ApiModel("项目举报表")
public class AttProjectInform implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

    /**
     * 举报内容
     */
    @ApiModelProperty("举报内容")
    private String informContent;

    /**
     * 工程id
     */
    @ApiModelProperty("工程id")
    private String projectId;

    /**
     * 发起人id
     */
    @ApiModelProperty("发起人id")
    private String initiatorId;

    /**
     * 发起人姓名
     */
    @ApiModelProperty("发起人姓名")
    private String initiator;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    @ApiModelProperty("是否删除 0-未删除 1-已删除")
    private Integer delFlag;

    public AttProjectInform() {
    }

}
