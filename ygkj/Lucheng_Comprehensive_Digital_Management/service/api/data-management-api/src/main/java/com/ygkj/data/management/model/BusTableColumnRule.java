package com.ygkj.data.management.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description 数据表具体字段规则
 * @date 2021-06-15
 */
@Data
@ApiModel("数据表具体字段规则")
public class BusTableColumnRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 表id，关联到bus_data_table
     */
    @ApiModelProperty("表id，关联到bus_data_table")
    private String tableId;

    /**
     * 字段名
     */
    @ApiModelProperty("字段名")
    private String columnName;

    @ApiModelProperty("字段中文释义")
    private String columnComment;

    /**
     * 规则id，关联到bus_data_rule
     */
    @ApiModelProperty("规则id，关联到bus_data_rule")
    private String ruleId;
    @ApiModelProperty("备注")
    private String remark;

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
    private boolean delFlag;

    public BusTableColumnRule() {
    }

}

