package com.ygkj.project.vo.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@ApiModel("报警管理列表查询接口返回字段释义")
@Data
public class WarningRecordResVo {

    /**
     * id，统一采用雪花算法策略
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 报警时间
     */
    @ApiModelProperty("报警时间")
    private Date warningTime;

    /**
     * 配置id
     */
    @ApiModelProperty("配置id")
    private String configId;

    @ApiModelProperty("报警类型")
    private String warningType;

    /**
     * 报警设备编号（摄像机编码/测站编码）
     */
    @ApiModelProperty("报警设备编号")
    private String equipmentCode;

    /**
     * 设备名称（摄像机名称/测站名称，冗余字段避免过多的级联查询）
     */
    @ApiModelProperty("报警设备名称")
    private String equipmentName;

    /**
     * 模板id
     */
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
    @ApiModelProperty("报警方式（钉钉/短信）")
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

    @ApiModelProperty("工程类型")
    private String projectType;

    @ApiModelProperty("报警模板内容")
    private String templateContent;
}
