package com.ygkj.flood.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-10-21 19:46
 */
@Data
@ApiModel("队伍设备信息")
public class AttTeamDeviceBase implements Serializable {
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

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("创建人")
    private String createId;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("修改人")
    private String updateId;

    @ApiModelProperty("是否删除 0-未删除 1-已删除")
    private int delFlag;

}
