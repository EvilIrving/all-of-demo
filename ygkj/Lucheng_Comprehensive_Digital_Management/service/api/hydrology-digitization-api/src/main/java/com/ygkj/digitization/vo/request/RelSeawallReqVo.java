package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class RelSeawallReqVo {

    @ApiModelProperty("测站编码")
    private String stCode;

    @ApiModelProperty("关联海塘的名称,逗号分隔")
    private String seawallCodes;

    @ApiModelProperty("备注")
    private String remark;
}
