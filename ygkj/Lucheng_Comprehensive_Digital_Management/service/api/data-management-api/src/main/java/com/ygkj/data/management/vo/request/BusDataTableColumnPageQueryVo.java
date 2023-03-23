package com.ygkj.data.management.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;

@Data
@ApiModel("数据资源列分页查询参数封装")
public class BusDataTableColumnPageQueryVo extends PageEntity {

    @ApiModelProperty("表id")
    private String tableId;

    @ApiModelProperty("字段中文名，可模糊查询")
    private String columnDesc;

    @ApiModelProperty("dataId，查单条用")
    private String dataId;

    @ApiModelProperty("查询参数json")
    private String paramJson;

    private String columnIds;

}
