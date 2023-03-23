package com.ygkj.data.management.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wd
 * @description 数据源
 * @date 2021-07-01
 */
@Data
@ApiModel("数据源")
public class BusDataSource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 数据源名称
     */
    @ApiModelProperty("数据源名称")
    private String dsName;

    /**
     * 是否自动 0-否 1-1是
     */
    @ApiModelProperty("是否自动 0-否 1-1是")
    private boolean isAuto;

    /**
     * 数据源审核状态 0-审核通过 1-待审核
     */
    @ApiModelProperty("数据源审核状态 0-审核通过 1-待审核")
    private boolean authStatus;

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
     * 是否删除 0-未删除
     */
    @ApiModelProperty("是否删除 0-未删除")
    private boolean delFlag;

    public BusDataSource() {
    }

}
