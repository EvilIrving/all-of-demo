package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author lxl
 * @create 2021-10-09 9:46
 * @description 旱情预警 分析参数
 */
@Data
@ApiModel
public class DroughtReqVo {

    /**
     * 查询日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("查询日期")
    private Date queryDate;

//    @ApiModelProperty("查询对应的分类")
//    private String type;
//
//    @ApiModelProperty("输入的参数值")
//    private String value;

    @ApiModelProperty("地区编码：计算连续无雨日数需求")
    private String areaCode;

    @ApiModelProperty("天气预报")
    private String weatherForecast;

    @ApiModelProperty("饮水困难人数：万人单位")
    private Integer drinkNum;

    @ApiModelProperty("供水紧张人数：万人单位")
    private Integer waterSupplyNum;

    @ApiModelProperty("乡村供水天数")
    private Integer ruralSupplyDays;

}
