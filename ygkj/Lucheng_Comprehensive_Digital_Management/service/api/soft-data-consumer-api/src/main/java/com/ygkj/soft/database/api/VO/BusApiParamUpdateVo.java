package com.ygkj.soft.database.api.VO;

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
@ApiModel("接口参数更新实体")
public class BusApiParamUpdateVo {
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
}
