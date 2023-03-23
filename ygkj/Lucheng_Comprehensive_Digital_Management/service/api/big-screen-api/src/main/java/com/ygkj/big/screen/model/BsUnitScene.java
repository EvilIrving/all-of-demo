package com.ygkj.big.screen.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 场景与组件关联表
 * @date 2021-06-29
 */
@Data
@ApiModel("场景与组件关联表")
public class BsUnitScene implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 场景id
     */
    @ApiModelProperty("场景id")
    private String sceneId;

    /**
     * 组件id
     */
    @ApiModelProperty("组件id")
    private String unitId;

    /**
     * 组件在大屏上的定位
     */
    @ApiModelProperty("组件在大屏上的定位")
    private String unitSceneProperty;

    public BsUnitScene() {
    }

}

