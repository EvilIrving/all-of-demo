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
public class BsUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 组件id
     */
    @ApiModelProperty("组件id,新增为空，更新不为空")
    private String id;

    /**
     * 模块名称
     */
    @ApiModelProperty("模块名称")
    private String unitName;

    /**
     * 大屏名称
     */
    @ApiModelProperty("大屏名称")
    private String bsName;

    /**
     * 预览url
     */
    @ApiModelProperty("预览url")
    private String unitPreviewUrl;

    /**
     * 组件属性
     */
    @ApiModelProperty("组件属性")
    private String unitProperty;

    public BsUnit() {
    }

}
