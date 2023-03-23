package com.ygkj.river.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/8/17
 */
@Data
@ApiModel("河长巡河问题统计")
public class ChiefPatrolProbStats {

    @ApiModelProperty("问题总数")
    Long totalNum;

    @ApiModelProperty("轻微")
    Long level0Num;

    @ApiModelProperty("一般问题数目")
    Long level1Num;

    @ApiModelProperty("重大问题数目")
    Long level2Num;

    @ApiModelProperty("已处理问题总数")
    Long handledNum;

    @ApiModelProperty("当年未处理总数")
    Long unHandledNum;
}
