package com.ygkj.river.vo.response;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hucong
 * @description 水域调查数据-水库临水线数据表
 * @date 2021-07-06
 */
@Data
@ApiModel("水域调查数据-水库临水线数据表")
public class AttRsalBaseRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    @ApiModelProperty("fid")
    private String fid;

    /**
     * 水库编码
     */
    @ApiModelProperty("水库编码")
    private String code;

    /**
     * geometry
     */
    @ApiModelProperty("geometry")
    private String geometry;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * shape_leng
     */
    @ApiModelProperty("shape_leng")
    private String shapeLeng;

    public AttRsalBaseRespVo() {
    }

}

