package com.ygkj.supervision.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class ProblemReqVo extends PageEntity {

    @ApiModelProperty("事项编码")
    private String matterCode;

    @ApiModelProperty("问题状态 -- 传参 1-已整改  0-未整改  其余-全部查询")
    private String problemStatus;

}
