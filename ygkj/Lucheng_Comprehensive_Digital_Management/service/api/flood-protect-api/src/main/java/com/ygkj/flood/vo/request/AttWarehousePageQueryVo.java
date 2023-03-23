package com.ygkj.flood.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AttWarehousePageQueryVo extends PageEntity {

    private static final long serialVersionUID = 3195103038752983313L;

    @ApiModelProperty("仓库类型")
    private String type;

    @ApiModelProperty("仓库名称")
    private String name;

    @ApiModelProperty("仓库结构")
    private String warehouseStructure;

    @ApiModelProperty("交通条件")
    private String trafficConditions;

    @ApiModelProperty("id，查询单条用")
    private String id;

    @ApiModelProperty("行政区划编码")
    private String areaCode;

    @ApiModelProperty("排序column列名")
    String orderColumn;

    @ApiModelProperty("升序降序,false asc/true desc")
    Boolean ascOrDesc = false;

    @ApiModelProperty("导出策略：0，大屏组件导出")
    Integer exportStrategy = 0;

}
