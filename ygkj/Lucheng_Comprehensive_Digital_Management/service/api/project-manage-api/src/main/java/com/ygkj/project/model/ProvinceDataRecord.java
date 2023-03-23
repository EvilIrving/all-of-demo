package com.ygkj.project.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ProvinceDataRecord {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("接口地址")
    private String url;

    @ApiModelProperty("接口描述")
    private String describe;

    @ApiModelProperty("时间")
    private Date tm;

    @ApiModelProperty("结果 0成功 1失败")
    private Integer result;

    @ApiModelProperty("返回值")
    private String res;

    @ApiModelProperty("请求参数")
    private String param;

}
