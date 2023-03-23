package com.ygkj.project.api.vo.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RelCountResVo {

    @ApiModelProperty("相关工程或站点类型 1水闸 2泵站 3闸站 4水位 5雨量 6摄像头")
    private Integer relType;

    @ApiModelProperty("数量")
    private Integer num;
}
