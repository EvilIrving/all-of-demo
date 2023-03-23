package com.ygkj.gragh.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@ApiModel("水库水位查询参数")
public class WaterLevelQueryVo {

    @ApiModelProperty("测站编码")
    @NotBlank(message = "请指定一个测站！")
    private String stationCode;

    @ApiModelProperty("测站类型：水库RR/河道ZZ/堰闸DD/墒情SS/泵站工情PT")
    private String stationType;

    @ApiModelProperty("往前推的天数")
    private int days;

    @ApiModelProperty("起始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("风暴潮编码")
    private String fcstId;

    @ApiModelProperty("测站名称")
    private String stationName;

    @ApiModelProperty("断面id")
    private String dmId;
}
