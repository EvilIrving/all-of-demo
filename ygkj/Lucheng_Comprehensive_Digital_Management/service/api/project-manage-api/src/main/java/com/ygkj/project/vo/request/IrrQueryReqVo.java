package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/9/14
 */
@ApiModel("灌区入参")
@Data
public class IrrQueryReqVo extends ProjectManageQueryVo {
    private static final long serialVersionUID = -3961051817414380336L;

    @ApiModelProperty("导出策略:0,大屏灌区组件详表导出")
    Integer exportStrategy = 0;

}
