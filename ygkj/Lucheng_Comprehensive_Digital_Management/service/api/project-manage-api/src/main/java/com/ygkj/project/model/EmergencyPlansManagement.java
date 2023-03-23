package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ygkj.gragh.model.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wgf
 * @date 2022/4/27 15:23
 * @Description: 预案管理
 */
@Data
public class EmergencyPlansManagement extends PageEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "工程编码")
    private String prcd;

    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "时间")
    private Date planTime;

    /**
     * 预案类型1.应急预案2.控运计划3.调度方案4.管理手册5.放水预警方案
     */
    @ApiModelProperty(value = "预案类型1.应急预案2.控运计划3.调度方案4.管理手册5.放水预警方案")
    private Integer planType;

    /**
     * 编制单位
     */
    @ApiModelProperty(value = "编制单位")
    private String organizationUnit;

    /**
     * 审批单位
     */
    @ApiModelProperty(value = "审批单位")
    private String approvalUnit;

    /**
     * 上传报告文件
     */
    @ApiModelProperty(value = "上传报告文件")
    private String uploadFiles;

    /**
     * 删除标记 0未删除 1已删除
     */
    @ApiModelProperty(value = "删除标记 0未删除 1已删除")
    private int delFlag;

    @TableField(exist = false)
    @ApiModelProperty(value = "编制单位名称")
    private String organizationUnitName;

    @TableField(exist = false)
    @ApiModelProperty(value = "审批单位名称")
    private String approvalUnitName;

    @TableField(exist = false)
    @ApiModelProperty(value = "工程名称")
    private String name;

    public EmergencyPlansManagement() {}
}
