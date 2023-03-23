package com.dcy.datamanage.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BusDataSetResField {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("字段名称")
    private String fieldName;

    @ApiModelProperty("字段类型0.未知 1.字符串 2.数值 3.整数 4.小数 5.布尔值 6.日期 7.时间 8.日期时间 9.对象 10.数组")
    private Integer fieldType;

    @ApiModelProperty("数据集id")
    private String dataSetId;

    @ApiModelProperty("字段描述")
    private String fieldDescribe;

    @ApiModelProperty("字段格式")
    private String fieldFormat;
}
