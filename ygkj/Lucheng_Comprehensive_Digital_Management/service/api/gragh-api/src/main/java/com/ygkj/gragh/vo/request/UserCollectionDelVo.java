package com.ygkj.gragh.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserCollectionDelVo {
    @ApiModelProperty("前端忽略")
    private String userId;
    @ApiModelProperty("类型：水位/雨量/...")
    private String type;
    @ApiModelProperty("编码")
    private String code;
}
