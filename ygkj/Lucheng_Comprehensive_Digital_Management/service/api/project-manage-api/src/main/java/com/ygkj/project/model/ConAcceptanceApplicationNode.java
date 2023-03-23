package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("合同验收流程节点")
public class ConAcceptanceApplicationNode implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Integer id;


    @ApiModelProperty("单位id")
    private String unitId;

    @ApiModelProperty("单位名称")
    @TableField(exist = false)
    private String unitName;

    @ApiModelProperty("单位负责人id")
    private String unitReviewerId;

    @ApiModelProperty("单位负责人名称")
    @TableField(exist = false)
    private String unitReviewerName;

    @ApiModelProperty("审批意见")
    private String comments;


    @ApiModelProperty("合同申请id")
    private String applicationid;


    @ApiModelProperty("节点状态，1-审批中 2-已驳回 3-通过")
    private String status;


    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("提醒状态 1-未提醒 2-已提醒")
    private String reminderStatus;

    @ApiModelProperty("删除标记 0未删除 1已删除")
    private int delFlag;


    @ApiModelProperty("创建时间")
    private Date createTime;


    @ApiModelProperty("创建人")
    private String createId;


    @ApiModelProperty("修改时间")
    private Date updateTime;


    @ApiModelProperty("修改人")
    private String updateId;
}
