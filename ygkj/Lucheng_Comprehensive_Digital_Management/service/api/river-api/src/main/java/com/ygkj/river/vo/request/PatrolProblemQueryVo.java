package com.ygkj.river.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/8/16
 */
@ApiModel("河长询问问题查询入参")
@Data
public class PatrolProblemQueryVo extends PageEntity {
    private static final long serialVersionUID = -1948104512434626044L;
    @ApiModelProperty("起始时刻")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date startTime;

    @ApiModelProperty("结束时刻")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date endTime;

    @ApiModelProperty("行政区划编码")
    String areaCode;

    @ApiModelProperty("状态[1:未受理;2:处理中;3:已处理待评价;4:已处理已评价]")
    Integer state;

    @ApiModelProperty("轻微程度  0 轻微 1 一般问题 2重大问题")
    Integer level;
}
