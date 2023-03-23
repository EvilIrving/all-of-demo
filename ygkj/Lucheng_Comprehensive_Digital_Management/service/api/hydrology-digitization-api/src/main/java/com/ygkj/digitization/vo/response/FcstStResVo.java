package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FcstStResVo {

    @ApiModelProperty("站点编码")
    private String stCode;

    @ApiModelProperty("站点名称")
    private String stName;
}
