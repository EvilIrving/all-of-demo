package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("关联潮位详细信息")
@Data
public class RelSeawallDetailResVo {

    @ApiModelProperty("海塘编码")
    private String seawallCode;

    @ApiModelProperty("海塘名称")
    private String seawallName;

    @ApiModelProperty("所属流域")
    private String bas;

    @ApiModelProperty("行政区划")
    private String adcd;

    @ApiModelProperty("行政区划名称")
    private String adnm;
}
