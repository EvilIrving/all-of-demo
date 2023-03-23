package com.ygkj.river.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 水域调查数据-山塘水面线数据表
 * @date 2021-07-06
 */
@Data
@ApiModel("水域调查数据-山塘水面线数据表")
public class AttHpwlBaseRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    @ApiModelProperty("fid")
    private String fid;

    /**
     * 山塘编码
     */
    @ApiModelProperty("山塘编码")
    private String code;

    /**
     * geometry
     */
    @ApiModelProperty("geometry")
    private String geometry;

    public AttHpwlBaseRespVo() {
    }

}

