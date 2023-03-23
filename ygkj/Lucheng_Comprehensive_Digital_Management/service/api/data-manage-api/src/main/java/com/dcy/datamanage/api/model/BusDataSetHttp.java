package com.dcy.datamanage.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BusDataSetHttp {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("数据集id")
    private String dataSetId;

    @ApiModelProperty("请求地址")
    private String requestUrl;

    @ApiModelProperty("请求方法")
    private String requestMethod;

    @ApiModelProperty("请求体方式 1.json 2.form-data")
    private Integer contentType;

    @ApiModelProperty("请求体json")
    private String requestBody;

    @ApiModelProperty("请求头json")
    private String requestHeader;

    @ApiModelProperty("响应JSON数据路径")
    private String responsePath;
}
