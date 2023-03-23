package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: fml
 * @Date: 2020/9/16 17:58
 * @Description:
 */
@Data
public class ProjectMatterPageReqVo {

    @ApiModelProperty(value = "第几页")
    private int pageNum=1;

    @ApiModelProperty(value = "分页数量")
    private int pageSize=10;

    @ApiModelProperty("事项名称")
    private String matterName;

    @ApiModelProperty("事项编码")
    private String matterNo;
}
