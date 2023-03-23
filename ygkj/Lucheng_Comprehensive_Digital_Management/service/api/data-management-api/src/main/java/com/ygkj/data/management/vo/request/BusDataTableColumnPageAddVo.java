package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;

@Data
@ApiModel("数据资源列分页查询参数封装")
public class BusDataTableColumnPageAddVo {

    @ApiModelProperty("表id")
    private String tableId;

    @ApiModelProperty("查询参数")
    private HashMap<String, String> params;


}
