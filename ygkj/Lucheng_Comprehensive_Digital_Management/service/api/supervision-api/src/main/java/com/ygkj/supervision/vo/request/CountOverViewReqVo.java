package com.ygkj.supervision.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lxl
 * @date 2021/05/12
 */
@Data
@ApiModel
public class CountOverViewReqVo {

    @ApiModelProperty("开始日期")
    private String start;

    @ApiModelProperty("结束日期")
    private String end;

    @ApiModelProperty("项目编码")
    private String projectCode;

    @ApiModelProperty("事项编码")
    private String matterCode;

    /**
     * 2021/05/24  统计分页首页试用参数
     */
    @ApiModelProperty("事项类别")
    private String matterType;

}
