package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fml
 * @date 2021-04-01 18:31
 * @description 接口基础信息表
 */
@Data
@ApiModel("接口基础信息表")
public class BusApiBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private String id;


    @ApiModelProperty("服务接口名称")
    private String apiName;


    @ApiModelProperty("请求方式")
    private String requestMethod;


    @ApiModelProperty("接口分类")
    private String apiCategory;


    @ApiModelProperty("接口类别")
    private String apiState;


    @ApiModelProperty("服务注册部门")
    private String signUpDepartment;


    @ApiModelProperty("数源部门")
    private String dataSourceDepartment;


    @ApiModelProperty("对外发布方法（对外url）")
    private String releaseMethod;


    @ApiModelProperty("来源地址（源url）")
    private String sourceUrl;


    @ApiModelProperty("接口说明")
    private String description;


    @ApiModelProperty("响应格式")
    private String responseFormat;


    @ApiModelProperty("请求格式")
    private String requestFormat;


    @ApiModelProperty("响应示例")
    private String responseExample;


    @ApiModelProperty("接口文档id，关联到系统附件表")
    private String apiDocumentId;


    @ApiModelProperty("接口状态（待上线/下线/正常）")
    private String status;


    @ApiModelProperty("责任人id")
    private String responsibleId;


    @ApiModelProperty("责任人")
    private String responsible;


    @ApiModelProperty("责任人电话")
    private String responsibleMobile;


    @ApiModelProperty("申请次数")
    private Integer applyNumber;


    @ApiModelProperty("创建时间")
    private String createTime;


    @ApiModelProperty("创建人")
    private String createId;


    @ApiModelProperty("修改时间")
    private String updateTime;


    @ApiModelProperty("修改人")
    private String updateId;


    @ApiModelProperty("是否删除 0-未删除 1-已删除")
    private int delFlag;
}
