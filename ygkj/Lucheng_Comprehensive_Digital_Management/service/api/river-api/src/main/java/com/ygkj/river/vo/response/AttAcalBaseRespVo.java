package com.ygkj.river.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 水域调查数据-人工水道临水线数据表
 * @date 2021-07-06
 */
@Data
@ApiModel("水域调查数据-人工水道临水线数据表")
public class AttAcalBaseRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    @ApiModelProperty("fid")
    private String fid;

    /**
     * 人工水道编码
     */
    @ApiModelProperty("人工水道编码")
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

    public AttAcalBaseRespVo() {
    }

}

