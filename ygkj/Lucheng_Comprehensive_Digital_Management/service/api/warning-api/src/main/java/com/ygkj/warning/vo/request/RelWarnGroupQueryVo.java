package com.ygkj.warning.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class RelWarnGroupQueryVo {

    @ApiModelProperty("预警组名称")
    private String name;

    @ApiModelProperty("要绑定的对象id")
    private String objId;
}
