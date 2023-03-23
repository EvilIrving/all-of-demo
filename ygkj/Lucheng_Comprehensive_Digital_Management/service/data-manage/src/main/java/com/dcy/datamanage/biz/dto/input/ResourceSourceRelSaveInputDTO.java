package com.dcy.datamanage.biz.dto.input;

import com.dcy.datamanage.api.model.BusDataResourceSourceRel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ResourceSourceRelSaveInputDTO {

    @ApiModelProperty("资源目录id")
    private String dataResourceId;

    @ApiModelProperty("表列表")
    private List<BusDataResourceSourceRel> relList;
}
