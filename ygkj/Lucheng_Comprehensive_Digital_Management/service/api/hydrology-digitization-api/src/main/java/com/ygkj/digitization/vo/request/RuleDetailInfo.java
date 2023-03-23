package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("预警规则详情")
public class RuleDetailInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    public RuleDetailInfo() {
    }

    /**
     * 小时
     */
    @ApiModelProperty("小时")
    private Integer hour;

    /**
     * 预警值
     */
    @ApiModelProperty("预警值")
    private BigDecimal overData;

    @ApiModelProperty("类型 1:时段雨量警报 2:日雨量警报-每天几时生成日雨量超过多少毫米 3:日雨量警报-每天几时全市面雨量大于等于多少毫米 " +
            " 5:河道水情警报-当前水位超过警戒水位（）米生成警报 6:河道水情警报-当前水位超过保证水位（）米生成警报 " +
            "7:水库水情警报-当前水位超过汛限水位（）米生成警报 8:水库水情警报-超过防洪高水位（）米 9:潮位警报-当前潮位超过（）米显示为蓝色预警 " +
            "10:当前潮位超过（）米显示为黄色预警 11:潮位警报-当前潮位超过（）米显示为橙色预警 12:潮位警报-当前潮位超过（）米显示为红色预警")
    private String type;

}
