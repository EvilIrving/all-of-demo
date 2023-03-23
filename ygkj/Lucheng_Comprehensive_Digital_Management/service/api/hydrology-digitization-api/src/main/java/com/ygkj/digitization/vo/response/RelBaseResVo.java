package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("旱情、水位关联列表出参")
public class RelBaseResVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("关联名称")
    private String relName;

    @ApiModelProperty("水库或测站的名称")
    private String psName;

    @ApiModelProperty("备注")
    private String remark;
}
