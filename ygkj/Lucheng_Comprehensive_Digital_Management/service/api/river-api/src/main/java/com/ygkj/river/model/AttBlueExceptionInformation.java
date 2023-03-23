package com.ygkj.river.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lxl
 * @create 2021-08-05 21:14
 * @description 蓝线异常信息配置条目表
 */
@Data
@ApiModel("蓝线异常信息配置条目表")
public class AttBlueExceptionInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 异常信息条目名称
     */
    @ApiModelProperty("异常信息条目名称")
    private String entryName;

    /**
     * 对应项目阶段 1-前期阶段 2-施工阶段 3-完工阶段
     */
    @ApiModelProperty("对应项目阶段 1-前期阶段 2-施工阶段 3-完工阶段")
    private Integer projectStage;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

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
    private Integer delFlag;

    public AttBlueExceptionInformation() {
    }

}
