package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PersonTrainReqVo extends PageEntity {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("年")
    private Integer year;

    @ApiModelProperty("工程编码")
    private String seawallCode;
}
