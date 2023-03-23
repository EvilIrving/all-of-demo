package com.ygkj.river.vo.response;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hucong
 * @description 水域调查数据-河道临水线数据表
 * @date 2021-07-06
 */
@Data
@ApiModel("水域调查数据-河道临水线数据表")
public class AttRvalBaseRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    @ApiModelProperty("fid")
    private String fid;

    /**
     * 河道（段）编码
     */
    @ApiModelProperty("河道（段）编码")
    private String code;

    /**
     * 左右岸
     */
    @ApiModelProperty("左右岸")
    private String side;

    /**
     * geometry
     */
    @ApiModelProperty("geometry")
    private String geometry;


    public AttRvalBaseRespVo() {
    }

}

