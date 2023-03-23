package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("表记录值查询参数封装")
public class BusDataCellDataValueQueryVo {

    @ApiModelProperty("单页限定记录数")
    private Integer pageSize;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("列id")
    @NotNull(message = "列id不能位空")
    private String colId;

    @ApiModelProperty("行id，即数据id")
    private String rowId;
}
