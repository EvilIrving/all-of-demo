package com.ygkj.project.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author fml
 * @date 2021-02-24 11:42
 * @description 工程外链信息
 */
@Data
public class AttProjLink implements Serializable {

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
     * 链接名（工程运管、洪水预报、巡查、安全监测）
     */
    @ApiModelProperty("链接名（工程运管、洪水预报、巡查、安全监测）")
    private String linkName;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
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
