package com.ygkj.flood.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-10-22 15:50
 */
@Data
@ApiModel("仓库物资信息")
public class AttWarehouseDeviceBaseReqVo{
    @ApiModelProperty("id")
    private Integer id;


    @ApiModelProperty("物资名称")
    private String name;


    @ApiModelProperty("物资类型")
    private String type;


    @ApiModelProperty("仓库id")
    private String warehouseId;


    @ApiModelProperty("数量")
    private Integer num;


    @ApiModelProperty("规格")
    private String measure;


    @ApiModelProperty("物资价值")
    private String materialPrice;

    @ApiModelProperty("页数")
    private Integer pageNum;

    @ApiModelProperty("页面大小")
    private Integer pageSize;
}
