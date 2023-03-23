package com.ygkj.soft.database.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description 接口分类表
 * @author wd
 * @date 2020-09-18
 */
@Data
@ApiModel("接口分类表 ")
public class ApiCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String categoryName;

    /**
     * 分类描述
     */
    @ApiModelProperty("分类描述")
    private String categoryRemark;

    /**
     * 上级id
     */
    @ApiModelProperty("上级id")
    private String parentId;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private String updateId;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 删除标记
     */
    @ApiModelProperty("删除标记")
    private Boolean deleted;

    public ApiCategory() {
    }

    /**
     * 子集
     */
    @ApiModelProperty("二级目录")
    private List<?> children;

}
