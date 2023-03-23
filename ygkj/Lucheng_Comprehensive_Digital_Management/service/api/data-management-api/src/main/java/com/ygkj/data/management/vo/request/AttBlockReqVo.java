package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author java.devtools.cn
 * @description 标段表
 * @date 2021-09-08
 */
@Data
@ApiModel("标段入参")
public class AttBlockReqVo implements Serializable {

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

    @ApiModelProperty(value = "标段ids", hidden = true)
    private String blockIds;

    @ApiModelProperty(value = "第几页")
    private Integer pageNum;

    @ApiModelProperty(value = "分页数量")
    private Integer pageSize;

    public AttBlockReqVo() {
    }

}