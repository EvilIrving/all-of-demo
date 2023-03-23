package com.ygkj.big.screen.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 大屏组件表
 * @date 2021-06-28
 */
@Data
@ApiModel("大屏组件表")
public class BsScene implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 场景id
     */
    @ApiModelProperty("场景id")
    private String id;

    /**
     * 场景名称
     */
    @ApiModelProperty("场景名称")
    private String sceneName;

    /**
     * 大屏名称
     */
    @ApiModelProperty("大屏名称")
    private String bsName;

    /**
     * 大屏是否是默认的0 是不默认的，1是默认的
     */
    @ApiModelProperty("大屏是否是默认的0 是不默认的，1是默认的 新增修改的时候请勿带上此参数")
    private Integer defaulted;

    /**
     * 大屏是否是默认的0 是不默认的，1是默认的
     */
    @ApiModelProperty("大屏是否是默认的0 是不默认的，1是默认的 新增修改的时候请勿带上此参数")
    private Integer editable;

    @ApiModelProperty("模式：1,大屏模式；2、PC模式")
    private Integer mode;

    @ApiModelProperty("当且仅当PC模式和用户关联")
    private String userId;

    @ApiModelProperty("当且仅当PC模式和用户关联")
    private boolean userDefaultScene = false;

    public BsScene() {
    }

}
