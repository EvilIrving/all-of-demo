package com.ygkj.river.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 水域调查数据-水库临水线点数据表
 * @date 2021-07-06
 */
@Data
@ApiModel("水域调查数据-水库临水线点数据表")
public class AttRsapBaseRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    @ApiModelProperty("fid")
    private String fid;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * 水库名称
     */
    @ApiModelProperty("水库名称")
    private String name;

    /**
     * 水库编码
     */
    @ApiModelProperty("水库编码")
    private String code;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private String lon;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private String lat;

    /**
     * 高程
     */
    @ApiModelProperty("高程")
    private String elevation;

    /**
     * geometry
     */
    @ApiModelProperty("geometry")
    private String geometry;


    public AttRsapBaseRespVo() {
    }

}
