package com.ygkj.data.management.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Huangzh
 * @description 数据操作审核表
 * @date 2021-03-23
 */
@Data
@ApiModel("数据操作审核表")
public class BusDataExamine implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 数据类型
     */
    @ApiModelProperty("数据类型")
    private String dataType;

    /**
     * 表名称
     */
    @ApiModelProperty("表名称")
    private String tableName;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updatePerson;

    /**
     * 状态：待审核/已通过/已驳回
     */
    @ApiModelProperty("状态：待审核/已通过/已驳回")
    private String status;

    /**
     * 审核理由
     */
    @ApiModelProperty("审核理由")
    private String examineReason;

    /**
     * 数据历史版本id
     */
    @ApiModelProperty("数据历史版本id")
    private String versionId;

    public BusDataExamine() {
    }

    public BusDataExamine(String id, String dataType, String tableName, String updatePerson, String status, String examineReason, String versionId) {
        this.id = id;
        this.dataType = dataType;
        this.tableName = tableName;
        this.updatePerson = updatePerson;
        this.status = status;
        this.examineReason = examineReason;
        this.versionId = versionId;
    }
}

