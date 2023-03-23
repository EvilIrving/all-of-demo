package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: huangzh
 * @Date: 2020/9/8 10:51
 * @Description:
 */
@Data
public class WaterBasePageReqVo {

    @ApiModelProperty(value = "第几页")
    private int pageNum=1;

    @ApiModelProperty(value = "分页数量")
    private int pageSize=10;
}
