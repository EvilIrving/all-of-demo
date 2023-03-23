package com.dcy.datamanage.biz.dto.output;

import com.dcy.datamanage.api.model.BusDataColumn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BusDataColumnOutputDTO extends BusDataColumn {

    @ApiModelProperty("列类型")
    private String typeName;

    @ApiModelProperty("数据类型")
    private Integer dataType;

    @ApiModelProperty("列长度")
    private Long columnSize;

    @ApiModelProperty("小数点长度")
    private Long decimalDigits;

    @ApiModelProperty("注释")
    private String remarks;

    @ApiModelProperty("是否可以为空 0否 1是")
    private Integer nullable;

    @ApiModelProperty("字段顺序")
    private Integer ordinalPosition;

    @ApiModelProperty("是否是主键 1是 0否")
    private Integer primary;

    @ApiModelProperty("显示名称")
    private String displayName;
}
