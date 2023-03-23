package com.ygkj.flood.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-10-22 15:51
 */
@Data
public class AttWarehouseDeviceBaseRespVo {
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
}
