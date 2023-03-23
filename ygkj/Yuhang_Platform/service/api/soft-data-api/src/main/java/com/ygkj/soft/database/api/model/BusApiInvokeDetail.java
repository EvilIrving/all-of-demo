package com.ygkj.soft.database.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *@描述  接口调用详情
 *@参数
 *@返回值
 *@创建人  wd
 *@创建时间  2020/9/17 21:26
 *@修改人和其它信息
 */

@Data
@ApiModel("接口调用日志详情")
public class BusApiInvokeDetail implements Serializable{
    /** 主键 */
    @ApiModelProperty("主键")
    private String id ;
    /** 日志id */
    @ApiModelProperty("日志id")
    private String logId ;
    /** 请求参数 */
    @ApiModelProperty("请求参数")
    private String invokeParam ;
    /** 请求结果 */
    @ApiModelProperty("请求结果")
    private String invokeResult ;
}
