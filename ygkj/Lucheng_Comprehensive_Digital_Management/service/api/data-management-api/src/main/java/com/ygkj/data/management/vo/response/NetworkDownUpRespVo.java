package com.ygkj.data.management.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hucong
 * @description 网络io表
 * @date 2021-07-17
 */
@Data
@ApiModel("网络io回参")
public class NetworkDownUpRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 上行速率(kb/s)
     */
    @ApiModelProperty("上行速率(kb/s)")
    private BigDecimal rxPercent;

    /**
     * 下行速率(kb/s)
     */
    @ApiModelProperty("下行速率(kb/s)")
    private BigDecimal txPercent;

    public NetworkDownUpRespVo() {
    }

}
