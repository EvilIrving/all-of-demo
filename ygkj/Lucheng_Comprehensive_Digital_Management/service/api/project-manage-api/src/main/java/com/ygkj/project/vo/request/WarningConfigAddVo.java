package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WarningConfigAddVo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id，修改时用")
    private String id;

    /**
     * 报警类型（摄像机/雨量/水位/台风）
     */
    @ApiModelProperty("报警类型（摄像机/雨量/水位/台风）")
    private String warningType;

    /**
     * 报警设备编号（摄像机编码/测站编码）
     */
    @ApiModelProperty("报警设备编号（摄像机编码/测站编码）")
    private String equipmentCode;

    /**
     * 设备名称（摄像机名称/测站名称，冗余字段避免过多的级联查询）
     */
    @ApiModelProperty("设备名称（摄像机名称/测站名称）")
    private String equipmentName;

    /**
     * 模板id
     */
    @ApiModelProperty("模板id")
    private String templateId;

    @ApiModelProperty("模板名称")
    private String templateName;

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
    @ApiModelProperty("0停用，1启用")
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
}
