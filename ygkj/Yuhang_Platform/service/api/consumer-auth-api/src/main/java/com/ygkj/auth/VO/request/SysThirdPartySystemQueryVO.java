package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Huang.zh
 * @date 2020/10/27 10:56
 * @Description:
 */
@Data
public class SysThirdPartySystemQueryVO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "系统名称")
    private String systemName;

    @ApiModelProperty(value = "第几页")
    private int pageNum=1;

    @ApiModelProperty(value = "分页数量")
    private int pageSize=10;
}
