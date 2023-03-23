package com.ygkj.flood.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/2/23
 */
@Data
@ApiModel("测站警戒信息")
public class WarnInfoResVo {

//    @ApiModelProperty("水库/工程编码")
//    String resCode;
//    @ApiModelProperty("水库/工程名称")
//    String resName;
//    @ApiModelProperty("测站编码")
//    String stCode;
//    @ApiModelProperty("测站名称")
//    String stName;
//    @ApiModelProperty("阈值")
//    Double threshold;
//    @ApiModelProperty("水位")
//    Double waterLevel;
//    @ApiModelProperty("降水")
//    Double drp;
//    @ApiModelProperty("降水或水位-阈值")
//    Double overThreshold;
//    @ApiModelProperty("时刻")
//    Date tm;

    @ApiModelProperty("编码")
    String code;
    @ApiModelProperty("名称")
    String name;
    @ApiModelProperty("阈值")
    Double threshold;
    @ApiModelProperty("实时数值")
    Double value;
    @ApiModelProperty("超阈值")
    Double overThreshold;
    @ApiModelProperty("时刻")
    Date time;
}
