package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("关联潮位列表")
public class RelSeawallListResVo {

    @ApiModelProperty("测站编码")
    private String stCode;

    @ApiModelProperty("测站名称")
    private String stName;

    @ApiModelProperty("关联的所有海塘名称")
    private String seawallNames;

    @ApiModelProperty("备注")
    private String seawallRemark;
}
