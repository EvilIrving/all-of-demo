package com.ygkj.supervision.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MatterReqVo extends PageEntity {

    /**
     * 事项名称
     */
    @ApiModelProperty("事项名称")
    private String matterName;

    @ApiModelProperty("工程编码")
    private String projectCode;

}
