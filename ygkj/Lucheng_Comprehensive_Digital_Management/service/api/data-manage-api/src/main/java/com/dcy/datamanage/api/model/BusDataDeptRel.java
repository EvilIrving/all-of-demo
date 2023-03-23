package com.dcy.datamanage.api.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BusDataDeptRel {

    @TableId
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("数据源id")
    private String dataSourceId;

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("只有主键的数据")
    private String primaryData;

    @ApiModelProperty("部门id")
    private String deptId;

    @ApiModelProperty("指派人id")
    private String createId;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
