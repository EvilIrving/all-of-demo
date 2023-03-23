package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SeawallWarnResVo {

    @ApiModelProperty("报警 1.正常，2.超蓝色3.超黄色,4.超橙色，5.超红色")
    private Integer warn;

    @ApiModelProperty("预警 1.正常，2.超蓝色3.超黄色,4.超橙色，5.超红色")
    private Integer earlyWarn;
}
