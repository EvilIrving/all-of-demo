package com.ygkj.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: huangzh
 * @Date: 2020/9/8 19:41
 * @Description:
 */
@Data
public class BusProjectPageReqVo {

    @ApiModelProperty(value = "第几页")
    private int pageNum=1;

    @ApiModelProperty(value = "分页数量")
    private int pageSize=10;

    @ApiModelProperty(value = "行政区划")
    String areaName;

    @ApiModelProperty(value = "项目类型")
    String projectProperty;

    @ApiModelProperty(value = "关键词")
    String keyword;

}
