package com.ygkj.soft.database.api.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author wd
 * @description 接口基础表
 * @date 2020-09-18
 */
@Data
@ApiModel("接口基础表")
public class BusApi implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

    /**
     * 接口名称
     */
    @ApiModelProperty("接口名称")
    private String apiName;

    /**
     * 对外地址
     */
    @ApiModelProperty("对外地址")
    private String outUrl;

    /**
     * 真实接口地址
     */
    @ApiModelProperty("真实接口地址")
    private String apiUrl;

    /**
     * 接口状态
     */
    @ApiModelProperty("接口状态")
    private String apiStatus;

    /**
     * 接口类型
     */
    @ApiModelProperty("接口类型 1-服务接口 2-数据接口")
    private Integer apiType;

    /**
     * 请求方式
     */
    @ApiModelProperty("请求方式")
    private Integer requestType;

    /**
     * 接口说明
     */
    @ApiModelProperty("接口说明")
    private String apiRemark;

    /**
     * 响应格式
     */
    @ApiModelProperty("响应格式")
    private Integer responseType;

    /**
     * 响应示例
     */
    @ApiModelProperty("响应示例")
    private String responseExample;

    /**
     * 归属部门
     */
    @ApiModelProperty("归属部门")
    private String belongDept;

    /**
     * 接口分类id
     */
    @ApiModelProperty("接口分类id")
    private String categoryId;

    /**
     * 接口分类
     */
    @ApiModelProperty("接口分类")
    private String category;

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

    /**
     * 一级目录
     */
    @ApiModelProperty("一级目录名称")
    private String categoryFst;

    /**
     * 二级目录
     */
    @ApiModelProperty("二级目录名称")
    private String categorySec;

    /**
     * 接口被调用次数
     */
    @ApiModelProperty("接口被调用次数")
    private Integer invokeTimes;

    /**
     * 接口被申请次数
     */
    @ApiModelProperty("接口被申请次数")
    private Integer applyTimes;

    public BusApi() {
    }

}
