package com.ygkj.project.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author fml
 * @date 2021-02-24 11:39
 * @description 工程建设内容信息
 */
@Data
public class AttProjBuildContent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，统一采用雪花算法
     */
    private String id;

    /**
     * 工程编码
     */
    @ApiModelProperty("工程编码")
    private String projCode;

    /**
     * 建设内容名称
     */
    @ApiModelProperty("建设内容名称")
    private String name;

    /**
     * 1.工程建设，2规划计划
     */
    @ApiModelProperty("1.工程建设，2规划计划")
    private String type;

    /**
     * pdf地址
     */
    @ApiModelProperty("pdf地址")
    private String url;

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
}
