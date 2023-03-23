package com.ygkj.project.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BeforeFloodResVo {

    @ApiModelProperty("汛前检查报告")
    private String aid;

    @ApiModelProperty("检查时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tm;
}
