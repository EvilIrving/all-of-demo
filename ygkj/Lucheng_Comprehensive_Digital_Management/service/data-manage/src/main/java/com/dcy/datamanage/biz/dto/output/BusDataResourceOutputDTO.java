package com.dcy.datamanage.biz.dto.output;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BusDataResourceOutputDTO {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("父id")
    private String parentId;

    @ApiModelProperty("资源名称")
    private String resourceName;

    @ApiModelProperty("资源子集")
    private List<BusDataResourceOutputDTO> subList;
}
