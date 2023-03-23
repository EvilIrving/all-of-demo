package com.ygkj.river.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 水域调查数据-船闸数据表
 * @date 2021-07-07
 */
@Data
@ApiModel("水域调查数据-船闸数据表")
public class AttPlkpBaseReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    @ApiModelProperty("fid")
    private String fid;

    /**
     * 船闸名称
     */
    @ApiModelProperty("船闸名称")
    private String name;

    /**
     * 所在水域名称
     */
    @ApiModelProperty("所在水域名称")
    private String wname;

    /**
     * 所在水域编码
     */
    @ApiModelProperty("所在水域编码")
    private String wcode;

    /**
     * 所在市
     */
    @ApiModelProperty("所在市")
    private String city;

    /**
     * 所在县(市、区)
     */
    @ApiModelProperty("所在县(市、区)")
    private String country;

    /**
     * 所在乡镇(街道)
     */
    @ApiModelProperty("所在乡镇(街道)")
    private String town;

    /**
     * 所在乡镇(经度)
     */
    @ApiModelProperty("所在乡镇(经度)")
    private String lon;

    /**
     * 所在乡镇(纬度)
     */
    @ApiModelProperty("所在乡镇(纬度)")
    private String lat;

    /**
     * geometry
     */
    @ApiModelProperty("geometry")
    private String geometry;

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("第几页")
    private Integer pageNum;

    public AttPlkpBaseReqVo() {
    }

}
