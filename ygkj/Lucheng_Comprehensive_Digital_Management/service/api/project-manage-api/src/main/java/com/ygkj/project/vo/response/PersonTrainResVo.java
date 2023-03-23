package com.ygkj.project.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PersonTrainResVo {

    @ApiModelProperty("年度")
    private Integer year;

    @ApiModelProperty("培训内容")
    private String content;

    @ApiModelProperty("培训经费")
    private BigDecimal fund;

    @ApiModelProperty("培训时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    @ApiModelProperty("培训人员")
    private String person;

    @ApiModelProperty("证明材料，多个用逗号分隔")
    private String aid;
}
