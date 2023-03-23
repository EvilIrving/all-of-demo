package com.ygkj.river.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 水域调查数据-人工水道临水线点数据表
 * @date 2021-07-06
 */
@Data
@ApiModel("水域调查数据-人工水道临水线点数据表")
public class AttAcapBaseReqVo implements Serializable {

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
     * 人工水道名称
     */
    @ApiModelProperty("人工水道名称")
    private String name;

    /**
     * 人工水道编码
     */
    @ApiModelProperty("人工水道编码")
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

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("第几页")
    private Integer pageNum;

    public AttAcapBaseReqVo() {
    }

}

