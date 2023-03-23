package com.ygkj.project.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description 工程巡查轨迹经纬度记录表
 * @author
 * @date 2022-04-28
 */
@Data
@ApiModel("工程巡查轨迹经纬度记录表")
public class BhSlPatrolGisInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 巡查记录编号
     */
    @ApiModelProperty("巡查记录编号")
    private Integer id;

    /**
     * 工程编号
     */
    @ApiModelProperty("工程编号")
    private String pjcd;

    /**
     * 轨迹记录（天地图）
     */
    @ApiModelProperty("轨迹记录（天地图）")
    private String position;

    /**
     * 源轨迹记录
     */
    @ApiModelProperty("源轨迹记录")
    private String positionSource;

    /**
     * 是否巡查完成
     */
    @ApiModelProperty("是否巡查完成")
    private Integer isComplete;

    /**
     * upload_position
     */
    @ApiModelProperty("upload_position")
    private String uploadPosition;

    /**
     * 巡查总长（米）
     */
    @ApiModelProperty("巡查总长（米）")
    private Double gisDistance;

    /**
     * 平均速度（米/秒）
     */
    @ApiModelProperty("平均速度（米/秒）")
    private Double avgSpeed;

    public BhSlPatrolGisInfo() {
    }

}
