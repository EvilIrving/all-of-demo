package com.ygkj.river.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 另一个湖库基础信息表
 * @date 2021-08-07
 */
@Data
@ApiModel("另一个湖库基础信息表")
public class AttRsvrLkBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 湖（库）编码
     */
    @ApiModelProperty("湖（库）编码")
    private String reservoirNo;

    /**
     * 湖（库）类型[1.水库;2.湖泊]
     */
    @ApiModelProperty("湖（库）类型[1.水库;2.湖泊]")
    private Integer reservoirType;

    /**
     * 湖（库）名称
     */
    @ApiModelProperty("湖（库）名称")
    private String reservoirName;

    /**
     * 湖（库）等级2:省级;3:市级;4:县级;5:镇级;6:村级
     */
    @ApiModelProperty("湖（库）等级2:省级;3:市级;4:县级;5:镇级;6:村级")
    private Integer level;

    /**
     * 所属流域
     */
    @ApiModelProperty("所属流域")
    private String watershedName;

    /**
     * 水域面积 湖泊总面积
     */
    @ApiModelProperty("水域面积 湖泊总面积")
    private String reservoirArea;

    /**
     * 水库容量 湖泊平均水深
     */
    @ApiModelProperty("水库容量 湖泊平均水深")
    private String reservoirDepth;

    /**
     * 湖库类型[(大型，中型，小(1)型，小(2)型)
     */
    @ApiModelProperty("湖库类型[(大型，中型，小(1)型，小(2)型)")
    private String type;

    /**
     * 所属地区
     */
    @ApiModelProperty("所属地区")
    private String areaPath;

    /**
     * 行政区划编码
     */
    @ApiModelProperty("行政区划编码")
    private String areaCode;

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

    public AttRsvrLkBase() {
    }

}
