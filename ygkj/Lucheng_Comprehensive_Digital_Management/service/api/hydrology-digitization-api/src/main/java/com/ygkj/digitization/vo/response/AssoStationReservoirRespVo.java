package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("水库关联测站回参")
public class AssoStationReservoirRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("测站编码")
    private String stCode;

    @ApiModelProperty("测站名称")
    private String stName;

    @ApiModelProperty("测站是否绑定 1:绑定 0:未绑定")
    private String bandFlag;

    public AssoStationReservoirRespVo() {
    }
}
