package com.ygkj.data.management.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("标段回参")
public class AttBlockRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 工程名称
     */
    @ApiModelProperty("工程名称")
    private String projectName;

    /**
     * 工程编码
     */
    @ApiModelProperty("工程编码")
    private String projectCode;

    /**
     * 标段名称
     */
    @ApiModelProperty("标段名称")
    private String blockName;

    /**
     * 是否授权 1：不授权 2：授权
     */
    @ApiModelProperty("是否授权 1：不授权 2：授权")
    private String authoriza;


    public AttBlockRespVo() {
    }

}