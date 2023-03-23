package com.ygkj.data.management.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description 服务调用日志
 * @date 2021-03-25
 */
@Data
@ApiModel("服务调用日志")
public class BusApiInvokeLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 服务id
     */
    @ApiModelProperty("服务id")
    private String apiId;

    /**
     * 服务名称
     */
    @ApiModelProperty("服务名称")
    private String apiName;

    /**
     * 入参
     */
    @ApiModelProperty("入参")
    private String requestJson;

    /**
     * 响应json
     */
    @ApiModelProperty("响应json")
    private String responseJson;

    /**
     * 调用时间
     */
    @ApiModelProperty("调用时间")
    private Date invokeTime;

    /**
     * 调用用户id
     */
    @ApiModelProperty("调用用户id")
    private String invokerId;

    /**
     * 调用用户
     */
    @ApiModelProperty("调用用户")
    private String invokerName;

    @ApiModelProperty("返回状态码")
    private String code;

    @ApiModelProperty("接口处理时长")
    private String duration;

    public BusApiInvokeLog() {
    }

    public BusApiInvokeLog(String apiId, String apiName, String requestJson,
                           String responseJson, Date invokeTime, String invokerId,
                           String invokerName, String code, String duration) {
        this.apiId = apiId;
        this.apiName = apiName;
        this.requestJson = requestJson;
        this.responseJson = responseJson;
        this.invokeTime = invokeTime;
        this.invokerId = invokerId;
        this.invokerName = invokerName;
        this.code = code;
        this.duration = duration;
    }
}

