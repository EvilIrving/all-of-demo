package com.ygkj.supervision.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lxl
 * @description att_supervision_matter_person
 * @date 2021-05-08
 */
@Data
@ApiModel("att_supervision_matter_person")
@TableName("att_supervision_matter_person")
public class AttSupervisionMatterPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联表id
     */
    @ApiModelProperty("关联表id")
    private String minId;

    /**
     * 检查事项编码
     */
    @ApiModelProperty("检查事项编码")
    private String matterCode;

    /**
     * 人员id
     */
    @ApiModelProperty("人员id")
    private String personId;

    /**
     * 人员名称
     */
    @ApiModelProperty("人员名称")
    private String personName;

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
    private String delFlag;

    public AttSupervisionMatterPerson() {
    }

}

