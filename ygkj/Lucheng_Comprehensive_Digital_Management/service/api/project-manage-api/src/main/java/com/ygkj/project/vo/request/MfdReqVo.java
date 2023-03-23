package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MfdReqVo extends PageEntity {

    @ApiModelProperty("年份")
    private Integer year;

    @ApiModelProperty("海塘编码")
    private String seawallCode;

    @ApiModelProperty("关联pid")
    private String pid;

}
