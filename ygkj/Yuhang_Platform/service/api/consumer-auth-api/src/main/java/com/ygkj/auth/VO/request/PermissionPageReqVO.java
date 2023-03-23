package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: PermissionPageReqVO
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/20 13:59
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/20 13:59
 * @Version: 0.0.1
 */
@Data
public class PermissionPageReqVO {
    @ApiModelProperty(value = "第几页")
    private int pageNum=1;

    @ApiModelProperty(value = "分页数量")
    private int pageSize=10;

    @ApiModelProperty(value = "1审批系统 2数据仓")
    private Integer system;
}
