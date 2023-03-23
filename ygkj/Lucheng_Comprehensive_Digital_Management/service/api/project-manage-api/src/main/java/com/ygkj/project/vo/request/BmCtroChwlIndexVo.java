package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wgf
 * @date 2022/3/23 11:19
 * @Description:
 */
@Data
public class BmCtroChwlIndexVo {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("工程编码")
    private String prcd;

    @ApiModelProperty("曲线类型：水库面积曲线、水库容积曲线")
    private String curtp;

}
