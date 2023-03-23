package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class TableAndCellInitVo {

    @ApiModelProperty("table_id")
    @NotNull(message = "tableId不能为空")
    private String tableId;

    @ApiModelProperty("表的概要字段，用于概要一条行记录得字段，如水库名称、测站名称")
    private String outLineColumn;

    @ApiModelProperty("数据源名称")
    private String dsName;

    @ApiModelProperty("创建人id（指的是数据源和数据列的创建人）")
    private String createId;

    @ApiModelProperty("创建时间（指的是数据源和数据列的创建时间）")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("责任部门id")
    private String dutyDepartId;

    @ApiModelProperty("责任部门名称")
    private String dutyDepartName;

    public TableAndCellInitVo(String tableId, String outLineColumn, String dsName, String createId, Date createTime, String dutyDepartId, String dutyDepartName) {
        this.tableId = tableId;
        this.outLineColumn = outLineColumn;
        this.dsName = dsName;
        this.createId = createId;
        this.createTime = createTime;
        this.dutyDepartId = dutyDepartId;
        this.dutyDepartName = dutyDepartName;
    }

    public TableAndCellInitVo() {
    }
}
