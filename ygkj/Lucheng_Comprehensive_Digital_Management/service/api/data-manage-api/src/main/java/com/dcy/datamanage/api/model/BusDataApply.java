package com.dcy.datamanage.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BusDataApply {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("数据源id")
    private String dataSourceId;

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("数据")
    private String dataObject;

    @ApiModelProperty("科室id")
    private String deptId;

    @ApiModelProperty("审批用户id")
    private String userId;

    @ApiModelProperty("申请类型 1.新增 2.删除 3.修改")
    private Integer applyType;

    @ApiModelProperty("申请结果 -1驳回 0待处理 1通过")
    private Integer applyStatus;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("创建人id")
    private String createId;

    @ApiModelProperty("备注")
    private String remark;
}
