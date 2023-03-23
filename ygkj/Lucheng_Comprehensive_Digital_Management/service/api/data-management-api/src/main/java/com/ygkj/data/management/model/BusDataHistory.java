package com.ygkj.data.management.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description 历史数据表
 * @date 2021-03-23
 */
@Data
@ApiModel("历史数据表")
public class BusDataHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 数据变化json
     */
    @ApiModelProperty("数据变化json")
    private String dataDetail;

    /**
     * 主键值，用于修改对应的数据
     */
    @ApiModelProperty("主键值，用于修改对应的数据")
    private String primaryValue;

    /**
     * 主键名
     */
    @ApiModelProperty("主键名")
    private String primaryKey;

    /**
     * 库
     */
    @ApiModelProperty("库")
    private String schemaName;

    /**
     * 表名
     */
    @ApiModelProperty("表名")
    private String tableName;

    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private String operationPerson;

    /**
     * 操作时间
     */
    @ApiModelProperty("操作时间")
    private String operationTime;

    /**
     * 录入平台名称
     */
    @ApiModelProperty("录入平台名称")
    private String platform;

    /**
     * 方式（手工/同步）
     */
    @ApiModelProperty("方式（手工/同步）")
    private String useWay;

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

    public BusDataHistory() {
    }

}

