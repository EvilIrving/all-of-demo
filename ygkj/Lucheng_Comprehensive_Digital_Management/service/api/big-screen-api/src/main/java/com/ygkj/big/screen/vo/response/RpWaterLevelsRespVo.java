package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fml
 * @date 2021-03-23 9:59
 * @description 水位列表信息
 */
@Data
public class RpWaterLevelsRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("测站代码")
    private String stCode;

    @ApiModelProperty("测站名称")
    private String stName;

    @ApiModelProperty("汛限水位")
    private Double limitLevel;

    @ApiModelProperty("警戒水位")
    private Double warningLevel;

    @ApiModelProperty("实时水位")
    private Double z;

    @ApiModelProperty("状态（0不正常，1正常）")
    private String status = "1";
}
