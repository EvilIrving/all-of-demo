package com.ygkj.project.vo.request;

import com.ygkj.project.model.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("水库工程入参数")
public class ReservoirProjectReq extends PageEntity implements Serializable {

    @ApiModelProperty("水库名称")
    private String resName;
    @ApiModelProperty("水库规模  1:大（Ⅰ）型 2:大（Ⅱ）型 3:中型 4:小（Ⅰ）型 5:小（Ⅱ）型")
    private String engScal;
    @ApiModelProperty("行政区划编码")
    private String areaCode;

}
