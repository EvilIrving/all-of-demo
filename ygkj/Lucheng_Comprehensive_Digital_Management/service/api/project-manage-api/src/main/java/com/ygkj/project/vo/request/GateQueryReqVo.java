package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/9/14
 */
@ApiModel("闸站入参")
@Data
public class GateQueryReqVo extends ProjectManageQueryVo {
    private static final long serialVersionUID = -7568920928793694849L;
    @ApiModelProperty("导出策略:0,大屏闸站组件详表导出")
    Integer exportStrategy = 0;

}
