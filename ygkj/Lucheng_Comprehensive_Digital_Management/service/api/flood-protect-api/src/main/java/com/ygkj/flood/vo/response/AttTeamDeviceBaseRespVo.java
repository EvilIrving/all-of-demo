package com.ygkj.flood.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-10-21 19:43
 */
@Data
public class AttTeamDeviceBaseRespVo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer id;


    @ApiModelProperty("设备名称")
    private String name;


    @ApiModelProperty("设备类型")
    private String type;


    @ApiModelProperty("队伍id")
    private String teamId;


    @ApiModelProperty("数量")
    private Integer num;


    @ApiModelProperty("单位")
    private String measure;
}
