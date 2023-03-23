package com.ygkj.project.vo.request;

import com.ygkj.project.model.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("海塘工程入参数")
public class SeawallProjectReq extends PageEntity implements Serializable {

    @ApiModelProperty("海塘名称")
    private String resName;
    @ApiModelProperty("海塘类型 1:1级 2:2级 3:3级 4:4级 5:5级")
    private String engScal;
    @ApiModelProperty("行政区划编码")
    private String areaCode;

}
