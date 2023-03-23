package com.ygkj.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 报警配置表
 *
 * @author huangzh 2020-12-30
 */
@ApiModel("报警配置列表查询接口返回字段释义")
@Data
@JsonIgnoreProperties(value = {"handler"})
public class WarningConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id，统一采用雪花算法策略
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 报警类型（摄像机/雨量/水位/台风）
     */
    @ApiModelProperty("摄像机/雨量/水位/台风")
    private String warningType;

    /**
     * 报警设备编号（摄像机编码/测站编码）
     */
    @ApiModelProperty("报警设备编号")
    private String equipmentCode;

    /**
     * 设备名称（摄像机名称/测站名称，冗余字段避免过多的级联查询）
     */
    @ApiModelProperty("设备名称")
    private String equipmentName;

    /**
     * 模板id
     */
    @ApiModelProperty("模板id")
    private String templateId;

    /**
     * 报警接收人名称（冗余字段避免过多的级联查询）
     */
    @ApiModelProperty("报警接收人名称")
    private String person;

    /**
     * 报警接收人电话（冗余字段避免过多的级联查询）
     */
    @ApiModelProperty("报警接收人电话")
    private String phone;

    /**
     * 报警方式（钉钉/短信）
     */
    @ApiModelProperty("报警方式")
    private String pattren;

    /**
     * 启用状态（0停用，1启用）
     */
    @ApiModelProperty("启用状态（0停用，1启用）")
    private boolean status;

    /**
     * 工程编码，冗余字段避免过多级联查询
     */
    @ApiModelProperty("工程编码")
    private String projectCode;
    /**
     * 工程名称，冗余字段避免过多级联查询
     */
    @ApiModelProperty("工程名称")
    private String projectName;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 删除标记
     */
    private boolean delFlag;

    /**
     * 模板内容
     */
    @ApiModelProperty("模板内容")
    private String templateContent;
    @ApiModelProperty("模板名称")
    private String templateName;

    @ApiModelProperty(value = "状态", hidden = true)
    private String openStatus;

    public void setStatus(boolean status) {
        this.status = status;
        this.openStatus = status ? "启用" : "停用";
    }

    public WarningConfig() {
    }

}

