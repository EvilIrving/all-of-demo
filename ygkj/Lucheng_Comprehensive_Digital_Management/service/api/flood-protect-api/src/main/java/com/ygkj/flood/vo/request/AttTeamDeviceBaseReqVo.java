package com.ygkj.flood.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-10-21 19:43
 */
@Data
@ApiModel("队伍设备信息")
public class AttTeamDeviceBaseReqVo{

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

    @ApiModelProperty("页数")
    private Integer pageNum;

    @ApiModelProperty("页面大小")
    private Integer pageSize;
}
