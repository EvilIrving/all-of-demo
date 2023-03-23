package com.ygkj.data.management.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DataQualityPageQueryVo extends PageEntity {

    @ApiModelProperty("表id")
    private String tableId;

    @ApiModelProperty("统计类型:不完整/未更新/低质量")
    private String type;
}
