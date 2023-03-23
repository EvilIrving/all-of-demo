package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class JobPersonReqVo extends PageEntity {

    @ApiModelProperty("海塘编码")
    private String seawallCode;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("主键")
    private String id;

}
