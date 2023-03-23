package com.dcy.datamanage.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BusDataSetParam {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("参数名称")
    private String paramName;

    @ApiModelProperty("是否必填 true(1)必填 false(0)不必填")
    private Integer required;

    @ApiModelProperty("参数描述")
    private String paramDescribe;

    @ApiModelProperty("数据集id")
    private String dataSetId;

    @ApiModelProperty("参数类型 1.字符串 2.数值 3.整数 4.小数 5.布尔值 6.日期 7.时间 8.日期时间 9.对象 10.数组")
    private Integer paramType;
}
