package com.ygkj.soft.database.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author fml
 * @date 2021-05-20 16:45
 * @description 数据考核-数据评分规则信息
 */
@Data
@ApiModel("数据考核-数据评分规则信息")
public class DataAssessScoreRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("更新频次id")
    private String freId;

    @ApiModelProperty("分数规则0~20的时间范围(以频次表时间单元为准)")
    private Integer column1;

    @ApiModelProperty("分数规则21~40的时间范围")
    private Integer column2;

    @ApiModelProperty("分数规则41~60的时间范围")
    private Integer column3;

    @ApiModelProperty("分数规则61~80的时间范围")
    private Integer column4;

    @ApiModelProperty("分数规则81~100的时间范围")
    private Integer column5;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
