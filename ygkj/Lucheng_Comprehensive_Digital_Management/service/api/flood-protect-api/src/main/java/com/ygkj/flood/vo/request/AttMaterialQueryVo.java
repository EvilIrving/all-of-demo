package com.ygkj.flood.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AttMaterialQueryVo {

    @ApiModelProperty("仓库id")
    private String warehouseId;

    @ApiModelProperty("物资种类id")
    private String typeId;
}
