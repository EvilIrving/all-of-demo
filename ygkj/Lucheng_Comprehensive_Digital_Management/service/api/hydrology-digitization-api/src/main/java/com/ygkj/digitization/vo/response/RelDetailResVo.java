package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("旱情、水位关联详细信息出参")
public class RelDetailResVo {

    @ApiModelProperty("关联关系id")
    private String relId;

    @ApiModelProperty("工程或者测站编码")
    private String psCode;

    @ApiModelProperty("工程或者测站名称")
    private String psName;

    @ApiModelProperty("流域")
    private String bas;

    @ApiModelProperty("行政区划编码")
    private String adcd;

    @ApiModelProperty("行政区划名称")
    private String adnm;
}
