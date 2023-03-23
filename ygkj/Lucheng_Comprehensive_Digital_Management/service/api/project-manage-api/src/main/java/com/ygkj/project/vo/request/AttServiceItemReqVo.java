package com.ygkj.project.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author lxl
 * @create 2022-04-12 11:24
 * @description
 */
@Data
@ApiModel
public class AttServiceItemReqVo extends PageEntity {


    /**
     * 服务事项主键
     */
    private String id;

    /**
     * 所属工程id
     */
    @ApiModelProperty("所属工程id")
    private String projectId;

    /**
     * 工程名称（冗余字段，减少联表）
     */
    @ApiModelProperty("工程名称")
    private String projectName;

    /**
     * 工程类型
     */
    @ApiModelProperty("工程类型")
    private String projectType;

    /**
     * 物业化公司id
     */
    @ApiModelProperty("物业化公司id")
    private String companyId;

    /**
     * 物业公司名称（冗余字段，减少联表）
     */
    @ApiModelProperty("物业公司名称（冗余字段，减少联表）")
    private String companyName;

    /**
     * 服务事项
     */
    @ApiModelProperty("服务事项")
    private String projectItem;

    /**
     * 服务开始时间
     */
    @ApiModelProperty("服务开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date serviceStartTime;

    /**
     * 服务结束时间
     */
    @ApiModelProperty("服务结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date serviceEndTime;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private String deptId;
}
