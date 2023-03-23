package com.ygkj.soft.database.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @描述 接口参数
 * @参数
 * @返回值
 * @创建人 wd
 * @创建时间 2020/9/17 21:25
 * @修改人和其它信息
 */

@Data
@ApiModel("接口参数")
public class BusApiParam implements Serializable {
    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;
    /**
     * 业务类型;1-请求参数，2-返回值参数
     */
    @ApiModelProperty("业务类型;1-请求参数，2-返回值参数 ")
    private Integer busType;
    /**
     * 接口id
     */
    @ApiModelProperty("接口id")
    private String apiId;
    /**
     * 参数名称
     */
    @ApiModelProperty("参数名称")
    private String paramName;
    /**
     * 参数说明
     */
    @ApiModelProperty("参数说明")
    private String paramDesc;
    /**
     * 参数类型
     */
    @ApiModelProperty("参数类型")
    private String paramType;
    /**
     * 是否必填
     */
    @ApiModelProperty("是否必填")
    private Boolean paramRequired;
    /**
     * 默认值
     */
    @ApiModelProperty("默认值")
    private String defaultValue;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createdBy;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createdTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private String updatedBy;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updatedTime;
    /**
     * 删除标记
     */
    @ApiModelProperty("删除标记")
    private Boolean deleted;

    public BusApiParam(String id, Integer busType, String apiId, String paramName, String paramDesc, String paramType, Boolean paramRequired, String defaultValue) {
        this.id = id;
        this.busType = busType;
        this.apiId = apiId;
        this.paramName = paramName;
        this.paramDesc = paramDesc;
        this.paramType = paramType;
        this.paramRequired = paramRequired;
        this.defaultValue = defaultValue;
    }

    public BusApiParam(String id, Integer busType, String apiId, String paramName, String paramDesc, String paramType) {
        this.id = id;
        this.busType = busType;
        this.apiId = apiId;
        this.paramName = paramName;
        this.paramDesc = paramDesc;
        this.paramType = paramType;
    }

    public BusApiParam() {
    }
}
