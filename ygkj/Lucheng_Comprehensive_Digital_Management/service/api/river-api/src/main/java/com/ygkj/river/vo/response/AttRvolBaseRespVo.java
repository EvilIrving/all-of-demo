package com.ygkj.river.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("水域调查数据-其他沟渠数据表")
public class AttRvolBaseRespVo implements Serializable {

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

    public AttRvolBaseRespVo() {
    }
}
