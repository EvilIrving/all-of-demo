package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("水库关联测站入参")
public class AssoStationReservoirReqVo implements Serializable {

    @ApiModelProperty("水库编码")
    private String projCode;

    @ApiModelProperty("每页展示条数")
    private int pageSize;

    @ApiModelProperty("页码")
    private int pageNum;
}
