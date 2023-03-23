package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Map;

/**
 * @author xq
 * @Description
 * @Date 2021/6/21
 */
@Data
@ApiModel("山区河道水位入参")
public class MrwlReqVo {
    @ApiModelProperty("时刻")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date dataTime;

    @ApiModelProperty("key是测站编码，value是曲线名称")
    Map<String, String> lnnmMap;


    @ApiModelProperty("流域")
    private String bas;

    @ApiModelProperty("行政区划编码")
    private String areaCode;

}
