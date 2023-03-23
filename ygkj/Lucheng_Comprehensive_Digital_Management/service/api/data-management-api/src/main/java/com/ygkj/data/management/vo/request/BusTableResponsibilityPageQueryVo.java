package com.ygkj.data.management.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BusTableResponsibilityPageQueryVo extends PageEntity {

    @ApiModelProperty("表id")
    private String tableIds;
}
