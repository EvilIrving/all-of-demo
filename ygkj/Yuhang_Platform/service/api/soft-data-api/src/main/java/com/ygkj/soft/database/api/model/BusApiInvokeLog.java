package com.ygkj.soft.database.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *@描述 接口调用日志
 *@参数
 *@返回值
 *@创建人  wd
 *@创建时间  2020/9/17 21:25
 *@修改人和其它信息
 */

@Data
@ApiModel("接口调用日志")
public class BusApiInvokeLog implements Serializable{
    /** 主键 */
    @ApiModelProperty("主键")
    private String id ;
    /** 接口id */
    @ApiModelProperty("接口id")
    private String apiId ;
    /** 调用者ip */
    @ApiModelProperty("调用者ip")
    private String invokeIp ;
    /** 调用者IP归属地 */
    @ApiModelProperty("调用者IP归属地")
    private String invokeIpRemark ;
    /** 调用结果;1-成功,0-失败 */
    @ApiModelProperty("调用结果;1-成功,0-失败")
    private Boolean invokeSatus ;
    /** 调用时间 */
    @ApiModelProperty("调用时间")
    private Date invokeTime ;
    /** 调用耗时(ms) */
    @ApiModelProperty("调用耗时(ms)")
    private Integer invokeSpend ;
    /** 数据包大小(b) */
    @ApiModelProperty("数据包大小(b")
    private Integer dataSize ;
    /** 调用用户id */
    @ApiModelProperty("调用用户id")
    private String invokeUserId ;
    /** 调用者密钥 */
    @ApiModelProperty("调用者密钥")
    private String accessKey ;
}