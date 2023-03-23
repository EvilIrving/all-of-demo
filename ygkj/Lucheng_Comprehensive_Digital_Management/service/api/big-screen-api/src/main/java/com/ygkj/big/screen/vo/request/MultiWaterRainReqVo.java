package com.ygkj.big.screen.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/5/13
 */
@Data
@ApiModel("根据时间区间查询多个测站水雨工情数据入参")
public class MultiWaterRainReqVo {

    @ApiModelProperty("测站编码")
    @NotBlank(message = "请至少指定一个测站！")
    private String stcds;

    @ApiModelProperty("起始时刻 格式：yyyy-MM-dd HH:mm:ss 起始时刻和结束时刻不能超过14天，若超过14天，则start失效并以end倒推14天为起始时刻")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date start;

    @ApiModelProperty("结束时刻 格式：yyyy-MM-dd HH:mm:ss 起始时刻和结束时刻不能超过2个月")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date end;

    @ApiModelProperty("潮位是否是天文潮位")
    private boolean isAstrotd = false;
}
