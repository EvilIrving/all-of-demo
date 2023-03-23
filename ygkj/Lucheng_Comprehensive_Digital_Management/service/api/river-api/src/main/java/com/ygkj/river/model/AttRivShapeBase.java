package com.ygkj.river.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 河道水质测站表
 * @date 2021-08-04
 */
@Data
@ApiModel("河道水质测站表")
public class AttRivShapeBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 河道编号
     */
    @ApiModelProperty("河道编号")
    private String riverNo;

    /**
     * 河段名称
     */
    @ApiModelProperty("河段名称")
    private String riverName;

    /**
     * 河流路径经纬度
     */
    @ApiModelProperty("河流路径经纬度")
    private String riverPath;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    @ApiModelProperty("是否删除 0-未删除 1-已删除")
    private Integer delFlag;

    public AttRivShapeBase() {
    }

}
