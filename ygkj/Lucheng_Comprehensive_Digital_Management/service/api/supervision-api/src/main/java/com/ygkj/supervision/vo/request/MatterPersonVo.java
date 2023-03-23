package com.ygkj.supervision.vo.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MatterPersonVo {


    @ApiModelProperty("检查事项人员编码")
    private String personCode;

    @ApiModelProperty("检查事项人员名称")
    private String personName;

}
