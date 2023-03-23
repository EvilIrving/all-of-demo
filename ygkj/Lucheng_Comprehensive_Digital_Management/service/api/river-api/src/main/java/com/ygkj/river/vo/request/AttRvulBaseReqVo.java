package com.ygkj.river.vo.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hucong
 * @description 水域调查数据-暗河数据表
 * @date 2021-07-06
 */
@Data
@ApiModel("水域调查数据-暗河数据表")
public class AttRvulBaseReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    @ApiModelProperty("fid")
    private String fid;

    /**
     * code
     */
    @ApiModelProperty("code")
    private String code;

    /**
     * name
     */
    @ApiModelProperty("name")
    private String name;

    /**
     * remark
     */
    @ApiModelProperty("remark")
    private String remark;

    /**
     * shape_length
     */
    @ApiModelProperty("shape_length")
    private String shapeLength;

    /**
     * geometry
     */
    @ApiModelProperty("geometry")
    private String geometry;

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("第几页")
    private Integer pageNum;

    public AttRvulBaseReqVo() {
    }

}

