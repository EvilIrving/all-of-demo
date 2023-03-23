package com.ygkj.digitization.vo.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 洪水风险图灾情数据表
 * @date 2021-07-21
 */
@Data
@ApiModel("洪水风险图灾情数据入参")
public class FloodRiskDisasterDataReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 设计洪水标准
     */
    @ApiModelProperty("设计洪水标准")
    private String designStandard;

    @ApiModelProperty("流域")
    private String basin;

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("第几页")
    private Integer pageNum;

    public FloodRiskDisasterDataReqVo() {
    }

}

