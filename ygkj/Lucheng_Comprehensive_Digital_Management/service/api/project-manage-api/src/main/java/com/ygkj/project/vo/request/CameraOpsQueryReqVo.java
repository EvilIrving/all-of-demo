package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/4/13
 */
@Data
public class CameraOpsQueryReqVo {
    @ApiModelProperty("运维人员姓名")
    String opsName;
    @ApiModelProperty("页码，最小为1，仅分页接口有效")
    int pageNum = 1;
    @ApiModelProperty("页大小，最小为1，仅分页接口有效")
    int pageSize = 10;
}
