package com.ygkj.data.management.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class RuleResultPageQueryVo extends PageEntity {

    @ApiModelProperty("测站名称")
    private String stationName;
    @ApiModelProperty("测站类型")
    private String stationType;

}
