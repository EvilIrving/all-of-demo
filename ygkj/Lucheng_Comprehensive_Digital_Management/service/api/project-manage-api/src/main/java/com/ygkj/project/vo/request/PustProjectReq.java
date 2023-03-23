package com.ygkj.project.vo.request;

import com.ygkj.project.model.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("泵站工程入参数")
public class PustProjectReq extends PageEntity implements Serializable {

    @ApiModelProperty("泵站编号")
    private String pustCode;
    @ApiModelProperty("泵站名称")
    private String pustName;
    @ApiModelProperty("泵站类型 1:大（Ⅰ）型 2:大（Ⅱ）型 3:中型 4:小（Ⅰ）型 5:小（Ⅱ）型")
    private String engScal;
    @ApiModelProperty("行政区划编码")
    private String areaCode;
    @ApiModelProperty("行政区划编码")
    private Integer exportStrategy = 0;

}
