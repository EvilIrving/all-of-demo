package com.dcy.datamanage.biz.dto.input;

import com.dcy.datamanage.biz.dto.output.BusDataColumnOutputDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DataEditInputDTO {

    @ApiModelProperty("数据源id")
    private String dataSourceId;

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("列信息")
    private List<BusDataColumnOutputDTO> colList;

    @ApiModelProperty(hidden = true)
    private String operateDeptId;

    @ApiModelProperty(hidden = true)
    private String operateUserId;

    @ApiModelProperty(hidden = true)
    private String roleType;
}
