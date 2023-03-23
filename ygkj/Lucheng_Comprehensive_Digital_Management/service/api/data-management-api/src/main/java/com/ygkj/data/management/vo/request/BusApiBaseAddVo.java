package com.ygkj.data.management.vo.request;

import com.ygkj.data.management.model.BusApiRequestParam;
import com.ygkj.data.management.model.BusApiResponseParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class BusApiBaseAddVo {

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 服务接口名称
     */
    @ApiModelProperty("服务接口名称")
    private String apiName;

    /**
     * 请求方式
     */
    @ApiModelProperty("请求方式")
    private String requestMethod;

    /**
     * 接口分类
     */
    @ApiModelProperty("接口分类")
    private String apiCategory;

    /**
     * 接口类别
     */
    @ApiModelProperty("接口类别")
    private String apiState;

    /**
     * 服务注册部门
     */
    @ApiModelProperty("服务注册部门")
    private String signUpDepartment;

    /**
     * 数源部门
     */
    @ApiModelProperty("数源部门")
    private String dataSourceDepartment;

    /**
     * 对外发布方法（对外url）
     */
    @ApiModelProperty("对外发布方法（对外url）")
    private String releaseMethod;

    /**
     * 来源地址（源url）
     */
    @ApiModelProperty("来源地址（源url）")
    private String sourceUrl;

    /**
     * 接口说明
     */
    @ApiModelProperty("接口说明")
    private String description;

    /**
     * 响应格式
     */
    @ApiModelProperty("响应格式")
    private String responseFormat;

    /**
     * 请求格式
     */
    @ApiModelProperty("请求格式")
    private String requestFormat;

    /**
     * 响应示例
     */
    @ApiModelProperty("响应示例")
    private String responseExample;

    /**
     * 接口文档id，关联到系统附件表
     */
    @ApiModelProperty("接口文档id")
    private String apiDocumentId;

//    /**
//     * 接口状态（待上线/下线/正常）
//     */
//    @ApiModelProperty("接口状态（待上线/下线/正常）")
//    private String status;

    /**
     * 责任人id
     */
    @ApiModelProperty("责任人id")
    private String responsibleId;

    /**
     * 责任人名称
     */
    @ApiModelProperty("责任人名称")
    private String responsible;

    /**
     * 责任人电话
     */
    @ApiModelProperty("责任人电话")
    private String responsibleMobile;

//    /**
//     * 申请次数
//     */
//    private Integer applyNumber;

    /**
     * 接口入参
     */
    @ApiModelProperty("接口入参的数组")
    List<BusApiRequestParam> requestParams;

    /**
     * 接口响应参数
     */
    @ApiModelProperty("接口响应参数的数组")
    List<BusApiResponseParam> responseParams;
}
