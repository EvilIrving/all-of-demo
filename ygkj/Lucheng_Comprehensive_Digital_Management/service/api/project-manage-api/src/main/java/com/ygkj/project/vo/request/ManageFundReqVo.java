package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ManageFundReqVo {

    @ApiModelProperty("海塘编码")
    private String seawallCode;

    @ApiModelProperty("年度")
    private Integer year;
}
