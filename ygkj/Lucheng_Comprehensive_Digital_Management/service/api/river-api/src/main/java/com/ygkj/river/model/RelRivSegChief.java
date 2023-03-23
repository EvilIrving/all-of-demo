package com.ygkj.river.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 河长通-河段、河长关联信息表
 * @date 2021-08-10
 */
@Data
@ApiModel("河长通-河段、河长关联信息表")
public class RelRivSegChief implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 河段id
     */
    @ApiModelProperty("河段id")
    private Integer rivSegId;

    /**
     * 河长id
     */
    @ApiModelProperty("河长id")
    private String chiefId;

    /**
     * 关系类型1：河长与河段的关联关系
     */
    @ApiModelProperty("关系类型1：河长与河段的关联关系")
    private Integer type;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    public RelRivSegChief() {
    }

}

