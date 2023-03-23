package com.ygkj.river.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 水域调查数据-其他水域水面线数据表
 * @date 2021-07-06
 */
@Data
@ApiModel("水域调查数据-其他水域水面线数据表")
public class AttOwwlBaseReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    @ApiModelProperty("fid")
    private String fid;

    /**
     * 其他水域编码
     */
    @ApiModelProperty("其他水域编码")
    private String code;

    /**
     * geometry
     */
    @ApiModelProperty("geometry")
    private String geometry;

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("第几页")
    private Integer pageNum;

    public AttOwwlBaseReqVo() {
    }

}
