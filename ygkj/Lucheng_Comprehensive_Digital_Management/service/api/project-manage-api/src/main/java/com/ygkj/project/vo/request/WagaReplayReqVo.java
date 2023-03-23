package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/6/3
 */
@Data
public class WagaReplayReqVo {
    @ApiModelProperty("水闸编号，多个水闸编号用半角逗号隔开")
    String wagaCodes;

    @ApiModelProperty("水闸编号，多个水闸编号用半角逗号隔开")
    String wagaName;

    @ApiModelProperty("测站测量时间点")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date start;

    @ApiModelProperty("测站测量时间点")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date end;

    @ApiModelProperty("工程行政区划编码")
    String wagaAdcd;

    @ApiModelProperty("流域：飞云江、瓯江、鳌江")
    String wagaBas;

    @ApiModelProperty("水闸工程等级：大（Ⅰ）型、大（Ⅱ）型、中型、小（Ⅰ）型、小（Ⅱ）型")
    String engScales;

    @ApiModelProperty("水闸条件查询字段，是否温瑞平水闸 是为true 否为false 不置可否 为null")
    private Boolean wrpWaga;

    @ApiModelProperty("排序column列名")
    String orderColumn;

    @ApiModelProperty("升序降序,false asc/true desc")
    boolean ascOrDesc = false;

    @ApiModelProperty("是否带工情曲线")
    boolean waterLevelCurve = false;
}
