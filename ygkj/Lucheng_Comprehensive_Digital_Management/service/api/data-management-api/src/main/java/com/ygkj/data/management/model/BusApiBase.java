package com.ygkj.data.management.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口基础信息表
 *
 * @author huang.zh 2021-03-17
 */
@Data
public class BusApiBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 服务接口名称
     */
    private String apiName;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 接口分类
     */
    private String apiCategory;

    /**
     * 接口类别
     */
    private String apiState;

    /**
     * 服务注册部门
     */
    private String signUpDepartment;

    /**
     * 数源部门
     */
    private String dataSourceDepartment;

    /**
     * 对外发布方法（对外url）
     */
    private String releaseMethod;

    /**
     * 来源地址（源url）
     */
    private String sourceUrl;

    /**
     * 接口说明
     */
    private String description;

    /**
     * 响应格式
     */
    private String responseFormat;

    /**
     * 请求格式
     */
    private String requestFormat;

    /**
     * 响应示例
     */
    private String responseExample;

    /**
     * 接口文档id，关联到系统附件表
     */
    private String apiDocumentId;

    /**
     * 接口状态（待上线/下线/正常）
     */
    private String status;

    /**
     * 责任人id
     */
    private String responsibleId;

    /**
     * 责任人名称
     */
    private String responsible;

    /**
     * 责任人电话
     */
    private String responsibleMobile;

    /**
     * 申请次数
     */
    private Integer applyNumber;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private boolean delFlag;

    private Integer apiType;

    private String tableId;

    public BusApiBase() {
    }


}

