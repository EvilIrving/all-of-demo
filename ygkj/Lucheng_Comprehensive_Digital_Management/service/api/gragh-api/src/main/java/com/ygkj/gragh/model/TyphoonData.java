package com.ygkj.gragh.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Huangzh
 * @description typhoon_data
 * @date 2020-12-09
 */
@Data
public class TyphoonData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Integer id;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private String beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private String endTime;

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String code;

    /**
     * 登陆地点
     */
    @ApiModelProperty("登陆地点")
    private String land;

    /**
     * 路径
     */
    @ApiModelProperty("路径")
    private String points;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private String createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Integer creatorId;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private String updateTime;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private Integer updatorId;

    /**
     * 删除标记 0未删除 1已删除
     */
    @ApiModelProperty("删除标记 0未删除 1已删除")
    private boolean delFlag;

    @ApiModelProperty("主要影响区域")
    private String affectedArea;
}
