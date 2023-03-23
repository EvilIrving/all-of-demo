package com.ygkj.river.vo.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hucong
 * @description 水域调查数据-河道中心线数据表
 * @date 2021-07-06
 */
@Data
@ApiModel("水域调查数据-河道中心线数据表")
public class AttRvclBaseReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    @ApiModelProperty("fid")
    private String fid;

    /**
     * 河道（段）名称
     */
    @ApiModelProperty("河道（段）名称")
    private String name;

    /**
     * 河道（段）编码
     */
    @ApiModelProperty("河道（段）编码")
    private String code;

    /**
     * 长度
     */
    @ApiModelProperty("长度")
    private String length;

    /**
     * geometry
     */
    @ApiModelProperty("geometry")
    private String geometry;

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("第几页")
    private Integer pageNum;

    public AttRvclBaseReqVo() {
    }

}

