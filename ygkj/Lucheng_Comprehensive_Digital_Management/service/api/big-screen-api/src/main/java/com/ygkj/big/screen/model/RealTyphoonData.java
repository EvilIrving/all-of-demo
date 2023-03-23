package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fml
 * @date 2021-03-31 19:07
 * @description 台风历史记录表（从四创获取
 */
@Data
@ApiModel("台风历史记录表（从四创获取）")
public class RealTyphoonData implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer id;


    @ApiModelProperty("台风名称")
    private String name;


    @ApiModelProperty("开始时间")
    private String beginTime;


    @ApiModelProperty("结束时间")
    private String endTime;


    @ApiModelProperty("登录点坐标")
    private String land;


    @ApiModelProperty("台风编号")
    private String code;


    @ApiModelProperty("路径数据")
    private String points;


    @ApiModelProperty("台风年份")
    private String year;


    @ApiModelProperty("登陆强度")
    private String landPower;


    @ApiModelProperty("英文名")
    private String englishName;
}
