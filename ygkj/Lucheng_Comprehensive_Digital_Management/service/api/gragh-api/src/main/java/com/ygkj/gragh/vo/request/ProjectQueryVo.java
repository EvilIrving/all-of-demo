package com.ygkj.gragh.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("工程参数查询封装")
public class ProjectQueryVo {

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("行政区划编码")
    private String areaCode;

    @ApiModelProperty("工程类型:大中型水库/小型水库/水库/泵站/水闸/海塘/堤防/山塘/水电站/农村供水工程/在建工程")
    private String type;
    @ApiModelProperty("工程编码，查询单条记录用")
    private String code;
    @ApiModelProperty("水库工程等别（仅查询水库时使用）:大（Ⅰ）型，大（Ⅱ）型，中型，小（Ⅰ）型，小（Ⅱ）型")
    private String level;

}
