package com.ygkj.river.vo.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hucong
 * @description 水域调查数据-其他沟渠数据表
 * @date 2021-07-06
 */
@Data
@ApiModel("水域调查数据-其他沟渠数据表")
public class AttRvolBaseReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    @ApiModelProperty("fid")
    private String fid;

    /**
     * 其他沟渠名称
     */
    @ApiModelProperty("其他沟渠名称")
    private String name;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * geometry
     */
    @ApiModelProperty("geometry")
    private String geometry;

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("第几页")
    private Integer pageNum;

    public AttRvolBaseReqVo() {
    }

}
