package com.ygkj.gragh.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WiuWptResVo {

    @ApiModelProperty("取水权人名称")
    private String masterName;

    @ApiModelProperty("法定代表人")
    private String lawPerson;

    @ApiModelProperty("取水类型")
    private String typeQslx;

    @ApiModelProperty("取水量")
    private BigDecimal wpermitsize;

    @ApiModelProperty("取水方式")
    private Integer intakeType;

    @ApiModelProperty("起始时间")
    private String intBd;

    @ApiModelProperty("结束时间")
    private String intDl;

    @ApiModelProperty("文件id")
    private String fileId;
}
