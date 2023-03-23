package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CountRelResVo {

    @ApiModelProperty("潮位站数量")
    private int ttNum;

    @ApiModelProperty("雨量站数量")
    private int ppNum;

    @ApiModelProperty("气象站数量")
    private int mmNum;

    @ApiModelProperty("摄像头数量")
    private int scNum;

    @ApiModelProperty("安全监测数量")
    private int smNum;
}
