package com.ygkj.supervision.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lxl
 * @description att_supervision_matter
 * @date 2021-05-08
 */
@Data
@TableName("att_supervision_matter")
public class AttSupervisionMatter implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事项编码
     */
    @ApiModelProperty("事项编码")
    @TableId("matter_code")
    private String matterCode;

    /**
     * 事项名称
     */
    @ApiModelProperty("事项名称")
    private String matterName;

    /**
     * 所属项目
     */
    @ApiModelProperty("所属项目")
    private String projectCode;

    /**
     * 项目名称（冗余字段，避免连表）
     */
    @ApiModelProperty("项目名称（冗余字段，避免连表）")
    private String projectName;

    /**
     * 事项状态（未下达-WX   已下达-YX  待审核-DS  已完成-YW ）
     */
    @ApiModelProperty("事项状态")
    private String matterStatus;

    /**
     * 检查事项（固定类别）
     */
    @ApiModelProperty("检查事项（固定类别）")
    private String matterType;

    /**
     * 检查时间
     */
    @ApiModelProperty("检查时间")
    private String censorDate;

    /**
     * 追究类型
     */
    @ApiModelProperty("追究类型")
    private String investigateType;

    /**
     * 被检查人员
     */
    @ApiModelProperty("被检查人员id")
    private String censorPerson;

    @ApiModelProperty("被检查人员姓名")
    private String censorName;

    @ApiModelProperty("被检查人员手机号码")
    private String censorPhone;

    @ApiModelProperty("被检查人员类别  0-对内  1-对外")
    private String censorType;

    @ApiModelProperty("流程id")
    private String processId;

    /**
     * 是否删除
     */
    @ApiModelProperty("是否删除")
    private String delFlag;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private String updateId;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    public AttSupervisionMatter() {
    }


    public AttSupervisionMatter(String matterCode, String matterName, String projectCode, String projectName, String matterType, String censorDate, String investigateType, String censorPerson, String censorPhone) {
        this.matterCode = matterCode;
        this.matterName = matterName;
        this.projectCode = projectCode;
        this.projectName = projectName;
        this.matterType = matterType;
        this.censorDate = censorDate;
        this.investigateType = investigateType;
        this.censorPerson = censorPerson;
        this.censorPhone = censorPhone;
    }
}

