package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 复盘收藏
 * @date 2021-06-16
 */
@Data
@ApiModel("复盘收藏回参")
public class BusDuplicateCollectionResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private String endTime;

    /**
     * 台风id
     */
    @ApiModelProperty("台风id")
    private String typhoonId;

    /**
     * 时间间隔
     */
    @ApiModelProperty("时间间隔")
    private String timeInterval;

    /**
     * 类型 1：台风 2： 自定义
     */
    @ApiModelProperty("类型 1：台风 2： 自定义")
    private String type;
    /**
     * 台风名称
     */
    @ApiModelProperty("台风名称")
    private String typhoonName;

    public BusDuplicateCollectionResp() {
    }
}