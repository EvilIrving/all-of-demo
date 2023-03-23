package com.ygkj.river.vo.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 水域调查数据-水库临水线数据表
 * @date 2021-07-06
 */
@Data
@ApiModel("水域调查数据-水库临水线数据表")
public class AttRsalBaseReqVo implements Serializable {

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
     * shape_leng
     */
    @ApiModelProperty("shape_leng")
    private String shapeLeng;

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("第几页")
    private Integer pageNum;

    public AttRsalBaseReqVo() {
    }

}

